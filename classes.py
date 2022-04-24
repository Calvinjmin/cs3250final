import random

class Card:
    def __init__(self, value, suit):
        self.value = value
        self.suit = suit

    def __str__(self):
        return self.value + ' ' + self.suit


class Deck:
    def __init__(self, cards):
        self.cards = cards

    def dealCard(self):
        random.shuffle(self.cards)
        return self.cards.pop(0)

    def printDeck(self):
        for c in self.cards:
            print(c)

    def setDeck(self, new_cards):
        self.cards = new_cards

    def getNumberCards( self ):
        return len(self.cards)
