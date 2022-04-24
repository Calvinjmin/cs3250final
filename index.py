import flask
from flask import Blueprint, render_template, g, request

from classes import Card, Deck

global deck
global fourDecks

idx = Blueprint('index', __name__, template_folder='templates', url_prefix='/')
suits = ['Hearts', 'Diamonds', 'Spades', 'Clubs']
card_value = ['Ace', '2', '3', '4', '5', '6', '7', '8', '9', '10', 'J', 'Q', 'K']
card_value_map = {'Ace': 1, '2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8,
                  '9': 9, '10': 10, 'J': 10, 'Q': 10, 'K': 10}

single_deck = [Card(value, suit) for value in card_value for suit in suits]
fourDecks = []
for i in range(4):
    for x in single_deck:
        fourDecks.append(x)

deck = Deck(fourDecks)

playerCard1 = None
playerCard2 = None
playerTotal = 0

dealerCard1 = None
dealerCard2 = None
dealerTotal = 0
gameTotal = 1000
s = False

card_data = {'playerCards': [playerCard1, playerCard2], 'dealerCards': [dealerCard1, dealerCard2]}
hand_totals = {'pTotal': playerTotal, 'dTotal': dealerTotal, 'gTotal':gameTotal}
show = {'show': s}
bet = {'betPlaced': False}

jsonCredits = {'credits': 100, 'betCredit' : 0}

@idx.route('/')
def index_page():
    if deck.getNumberCards < 10:
        resetDeck()
    return render_template('index.html', jsonCredits = jsonCredits , card_data=card_data, hand_totals=hand_totals, show=show)


@idx.route('/subCredits')
def resetCredits():
    if bet['betPlaced'] == False and jsonCredits['credits'] <= 0:
        jsonCredits['credits'] = 100
    return flask.redirect(flask.url_for('index.index_page'))

@idx.route('/resetCredits')
def subCredits():
    if bet['betPlaced'] == False and (jsonCredits['credits'] - 10) >= 0:
        jsonCredits['credits'] = int(jsonCredits['credits']) - 10
    return flask.redirect(flask.url_for('index.index_page'))


@idx.route('/addCredits')
def addCredits():
    if bet['betPlaced'] == False:
        jsonCredits['credits'] = int(jsonCredits['credits']) + 10
    return flask.redirect(flask.url_for('index.index_page'))


@idx.route('/placeBet',  methods=('GET', 'POST'))
def placeBet():
    if request.method == 'POST':
        betAmount = int(request.form['betDropdown'])
        if betAmount <= int(jsonCredits['credits']) and bet['betPlaced'] == False:
            jsonCredits['credits'] -= betAmount
            jsonCredits['betCredit'] = betAmount
            bet['betPlaced'] = True
            reset_hand()

    return flask.redirect(flask.url_for('index.index_page'))


def resetDeck():
    fourDecks = []
    for i in range(4):
        for x in single_deck:
            fourDecks.append(x)

    deck = Deck(fourDecks)
    deck.setDeck(fourDecks)

@idx.route('/bj/reset')
def reset_hand():
    if bet['betPlaced'] == True:
        fourDecks = []
        for i in range(4):
            for x in single_deck:
                fourDecks.append(x)

        deck = Deck(fourDecks)
        deck.setDeck(fourDecks)

        show['show'] = False
        newPC1 = deck.dealCard()
        newPC2 = deck.dealCard()
        newGTotal = 50
        newPTotal = int(card_value_map[newPC1.value]) + int(card_value_map[newPC2.value])
        pc1val = card_value_map.get(newPC1.value)
        pc2val = card_value_map.get(newPC2.value)
        if((pc1val == 1 and pc2val == 10) or (pc1val == 10 and pc2val == 1)):
            newGTotal = 100
            newPTotal = 21
            jsonCredits['credits'] += 3 * int(jsonCredits['betCredit'])
            bet['betPlaced'] = False
            show['show'] = True


        newDC1 = deck.dealCard()
        newDC2 = deck.dealCard()
        dc1val = card_value_map.get(newDC1.value)
        dc2val = card_value_map.get(newDC2.value)
        newDTotal = int(card_value_map[newDC1.value]) + int(card_value_map[newDC2.value])

        if ((dc1val == 1 and dc2val == 10) or (dc1val == 10 and dc2val == 1)):
            # both got BJ so push
            if (newGTotal == 100):
                newGTotal = 0
                newDTotal = 21
                show['show'] = True
            else:
                newGTotal = -100
                newDTotal = 21
                show['show'] = True
            bet['betPlaced'] = False

        card_data['playerCards'] = [newPC1, newPC2]
        card_data['dealerCards'] = [newDC1, newDC2]
        hand_totals['pTotal'] = newPTotal
        hand_totals['dTotal'] = newDTotal
        hand_totals['gTotal'] = newGTotal

    return flask.redirect(flask.url_for('index.index_page'))


@idx.route('/bj/hit')
def hit():
    if bet['betPlaced'] == True:
        if (abs(hand_totals['gTotal']) != 100 or hand_totals != 0) and hand_totals['pTotal'] < 21 and hand_totals['gTotal'] != -1 and show['show'] != True:
            if hand_totals['pTotal'] == -1:
                return flask.redirect(flask.url_for('index.index_page'))

            hitCard = deck.dealCard()
            card_data['playerCards'].append(hitCard)
            hand_totals['pTotal'] = int(hand_totals['pTotal']) + int(card_value_map[hitCard.value])
            # bust
            if hand_totals['pTotal'] > 21:
                hand_totals['gTotal'] = -1
                bet['betPlaced'] = False
                show['show'] = True
            if (hand_totals['pTotal'] == 21):
                stand()
    return flask.redirect(flask.url_for('index.index_page'))


@idx.route('/bj/stand')
def stand():
    if bet['betPlaced'] == True:
        show['show'] = True
        if abs(hand_totals['gTotal']) != 100 and hand_totals['gTotal'] != 0 and hand_totals['gTotal'] != -1:
            while (hand_totals['dTotal'] < 17 and hand_totals['gTotal'] != 1):
                hitCard = deck.dealCard()
                card_data['dealerCards'].append(hitCard)
                hand_totals['dTotal'] = int(hand_totals['dTotal']) + int(card_value_map[hitCard.value])
                if hand_totals['dTotal'] > 21:
                    hand_totals['gTotal'] = 1
            # dealer bust
            if hand_totals['dTotal'] > 21:
                jsonCredits['credits'] += 2*int(jsonCredits['betCredit'])
                hand_totals['gTotal'] = 1
            # dealer wins
            elif hand_totals['dTotal'] > hand_totals['pTotal']:
                hand_totals['gTotal'] = -2
            # push
            elif hand_totals['dTotal'] == hand_totals['pTotal']:
                jsonCredits['credits'] += int(jsonCredits['betCredit'])
                hand_totals['gTotal'] = 0
            else:
                # win
                hand_totals['gTotal'] = 2
                jsonCredits['credits'] += 2 * int(jsonCredits['betCredit'])
        bet['betPlaced'] = False
    return flask.redirect(flask.url_for('index.index_page'))
