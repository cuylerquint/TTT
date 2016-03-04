'''
Created on Sep 20, 2015

@author: Cuyler Quint <deanquint@gmail.com>


Tic Tac Toe
    Requirements
        -python version 1.7
        -pygame version 1.9

    Description:
        This is a clone of the famous tic tac Toe
        with implemetation of a computer to play agaisnt
        at three different levels.

        level 1: random moves
        level 2: blocks winning moves
        level 3: level 2 and attemps to win

        command line arguments are very straigt forward. Enjoy!

'''



import random
import pygame

class Player:

    def __init__(self, name, letter):
        self.name = name
        self.letter = letter

class Game:
    '''
    classdocs
    '''

    def __init__(self,user,AI):
        self.user = user
        self.AI = AI
        self.Difficulty = input("Welcome to Tic Tac Toe\n\nSelect Difficulty\n1)level 0\n2)level 1\n3)level 2\n")
        self.TTTbox = [' '] * 9
        self.Playing = True
        self.turnCount = 0
        self.UserLetter = 'X'
        self.AILetter = 'O'
        self.turn = 'user'

        '''
        Constructor
        '''
    def gameName(self):
        print(self.AI.letter)
        print(self.user.letter)
        print(self.Difficulty)

    def setGame(self):
        '''
        Sets the Game for starting play
        '''
        self.TTTbox[0] = '1'
        self.TTTbox[1] = '2'
        self.TTTbox[2] = '3'
        self.TTTbox[3] = '4'
        self.TTTbox[4] = '5'
        self.TTTbox[5] = '6'
        self.TTTbox[6] = '7'
        self.TTTbox[7] = '8'
        self.TTTbox[8] = '9'
        self.Playing = True
        self.turn = 'user'
        self.turnCount = 0


    def printBox(self,box):
        '''
        prints the updated board with user and computer moves
        '''
        print(' ' + box[0] + ' | ' + box[1] + ' | ' + box[2])
        print('-----------')
        print(' ' + box[3] + ' | ' + box[4] + ' | ' + box[5])
        print('-----------')
        print(' ' + box[6] + ' | ' + box[7] + ' | ' + box[8])


    def copyBox(self,box):
        '''
        copys a temporary board and returns that board
        '''
        copyBox = []
        for i in box:
            copyBox.append(i)
        return copyBox

    def userPick(self,TTTbox,Char):
        '''
        goes throught a users turn and promts them to make
        a move and updates ther choice if valid
        '''
        print('Pick a avaible space')
        pick = input()
        if (self.openSpace(TTTbox, pick) == False ):
            print('spot already taken, pick again')
            self.printBox(TTTbox)
            self.userPick(TTTbox,Char)
        self.paintBox(TTTbox,Char , pick)
        return pick

    def getPossibleMoves(self,box):
        '''
        determines all possible moves that a user
        can pick from and returns array of them
        '''
        pMoves = [1,2,3,4,5,6,7,8,9]
        for i in range(1,10):
            if ((box[i-1] == 'X') or (box[i-1] == 'O')):
                pMoves.remove(i)
        return pMoves


    def getWinningMove(self,box):
        '''
        copys a box for all possible moves
        and test if a winning move can be made
        '''
        for i in range(1,10):
            cBox = self.copyBox(box)
            if (self.openSpace(cBox, i) == True):
                self.paintBox(cBox,'O' , i)
                if self.Win(cBox, 'O'):
                    return i
        return 0


    def getBlockingMove(self,box):
        '''
        copys a box for all possible moves
        and tests if a blocking move can be made
        '''

        for i in range(1,10):
            cBox = self.copyBox(box)
            if (self.openSpace(cBox, i) == True):
                self.paintBox(cBox,'X' , i)
                if self.Win(cBox, 'X'):
                    return i
        return 0

    def openSpace(self,TTTbox, move):
        '''
        determines if a positon is a open space or not
        '''
        temp = move - 1
        if ((TTTbox[temp] ==  'O') or (TTTbox[temp] ==  'X')):
            return False
        else:
            return True


    def getPendingMove(self,box):
        '''
        logic to place best move by computer in
        early game that was not chosen by human
        '''
        if((box[5] == 'O') and((box[1] == 'O') or (box[3] == 'O') or (box[7] == 'O') or (box[9] == 'O'))):
            for i in range(1,9):
                if (i == 2):  # take middle
                    if (self.openSpace(box, 2) == True):
                        return i;
                if (i == 4):  # take middle
                    if (self.openSpace(box, 4) == True):
                        return i;
                if (i == 6):  # take korner
                    if (self.openSpace(box, 6) == True):
                        return i;
                if (i == 8):  # take korner
                    if (self.openSpace(box, 8) == True):
                        return i;
                else:
                    return 0;

#  made random move at 2 4 6 or 8
        if (self.openSpace(box, 5) == True):
            return 5
        else:
            for i in range(1,9):
                if (i == 1):  # take korner
                    if (self.openSpace(box, 1) == True):
                         return 1;
                if (i == 3):  # take korner
                    if (self.openSpace(box, 3) == True):
                         return 3;
                if (i == 7):  # take korner
                    if (self.openSpace(box, 7) == True):
                         return 7;
                if (i == 9):  # take korner
                    if (self.openSpace(box, 9) == True):
                        return 9;

            else:
                return 0;


    def makeAIMove(self,box,char,difficulty):
        '''
        depending on the computers difficulty, this function decides
        what type of move the computer will make based of the
        known played postions.
        '''
        if self.Difficulty == 1:
            pMoves = self.getPossibleMoves(box)
            move = random.choice(pMoves)
            self.paintBox(box, char, move)
        elif self.Difficulty == 2 :#see if can finsish, make random
            wMove = self.getWinningMove(box)
            if(wMove == 0):
                pMoves = self.getPossibleMoves(box)
                move = random.choice(pMoves)
                self.paintBox(box, char, move)
            else:
                self.paintBox(box, char, wMove)
        elif self.Difficulty == 3:#make pending move  do three board copys and make each use and comp move
            wMove = self.getWinningMove(box)
            if(wMove == 0):
                bMove = self.getBlockingMove(box)
                if(bMove == 0):
                    pendingMove = self.getPendingMove(box)
                    if(pendingMove == 0):
                        pMoves = self.getPossibleMoves(box)
                        move = random.choice(pMoves)
                        self.paintBox(box, char, move)
                    else:
                        self.paintBox(box, char, pendingMove)
                else:
                    self.paintBox(box, char, bMove)
            else:
                self.paintBox(box, char, wMove)


    def fullBox(self,box):
        '''
        determines if a game has all postions filled
        '''
        for i in range(1,10):
            if (self.openSpace(box, i)):
                return False
            return True

    def paintBox(self,box, letter, move):
        '''
        paints the desginated postion to the
        board with the correct player letter
        '''

        if (move == 9):
            box[8] = letter
        temp = move - 1
        box[temp] = letter

    def Win(self,box,char):
        '''
        checks made to see if a player has won
        '''
        if((box[0] == char and box[1] == char and box[2] == char) or
        (box[3] == char and box[4] == char and box[5] == char ) or
        (box[6] == char and box[7] == char and box[8] == char ) or #hori
        (box[0] == char and box[3] == char and box[6] == char ) or
        (box[1] == char and box[4] == char and box[7] == char ) or
        (box[2] == char and box[5] == char and box[8] == char ) or #vert
        (box[0] == char and box[4] == char and box[8] == char ) or
        (box[6] == char and box[4] == char and box[2] == char )):
            return True#diag
        else:
            return False
