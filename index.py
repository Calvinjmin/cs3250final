import flask
from flask import Blueprint, render_template

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
gameTotal = 0
s = False

card_data = {'playerCards': [playerCard1, playerCard2], 'dealerCards': [dealerCard1, dealerCard2]}
hand_totals = {'pTotal': playerTotal, 'dTotal': dealerTotal, 'gTotal':gameTotal}
show = {'show': s}


@idx.route('/')
def index_page():
    return render_template('index.html', card_data=card_data, hand_totals=hand_totals, show=show)


@idx.route('/bj/reset')
def reset_hand():
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

    newDC1 = deck.dealCard()
    newDC2 = deck.dealCard()
    dc1val = card_value_map.get(newDC1.value)
    dc2val = card_value_map.get(newDC2.value)
    newDTotal = int(card_value_map[newDC1.value]) + int(card_value_map[newDC2.value])

    if((dc1val == 1 and dc2val == 10) or (dc1val == 10 and dc2val == 1)):
        newGTotal = -100
        newDTotal = 21
        show['show'] = True

    card_data['playerCards'] = [newPC1, newPC2]
    card_data['dealerCards'] = [newDC1, newDC2]
    hand_totals['pTotal'] = newPTotal
    hand_totals['dTotal'] = newDTotal
    hand_totals['gTotal'] = newGTotal

    return flask.redirect(flask.url_for('index.index_page'))


@idx.route('/bj/hit')
def hit():
    if abs(hand_totals['gTotal']) != 100:
        if hand_totals['pTotal'] == -1:
            return flask.redirect(flask.url_for('index.index_page'))

        hitCard = deck.dealCard()
        card_data['playerCards'].append(hitCard)
        hand_totals['pTotal'] = int(hand_totals['pTotal']) + int(card_value_map[hitCard.value])
        if hand_totals['pTotal'] > 21:
            hand_totals['gTotal'] = -1
        if (hand_totals['pTotal'] == 21):
            stand()
    return flask.redirect(flask.url_for('index.index_page'))

@idx.route('/bj/stand')
def stand():
    show['show'] = True
    if abs(hand_totals['gTotal']) != 100:
        while(hand_totals['dTotal'] < 17 and hand_totals['gTotal'] != 1):
            hitCard = deck.dealCard()
            card_data['dealerCards'].append(hitCard)
            hand_totals['dTotal'] = int(hand_totals['dTotal']) + int(card_value_map[hitCard.value])
            if hand_totals['dTotal'] > 21:
                hand_totals['gTotal'] = 1
        if hand_totals['dTotal'] > 21:
            hand_totals['gTotal'] = 1
        elif hand_totals['dTotal'] > hand_totals['pTotal']:
            hand_totals['gTotal'] = -2
        elif hand_totals['dTotal'] == hand_totals['pTotal']:
            hand_totals['gTotal'] = 0
        else:
            hand_totals['gTotal'] = 2
    return flask.redirect(flask.url_for('index.index_page'))