#!/usr/bin/env python
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db ,auth
import re, datetime
import sys, os

cred = credentials.Certificate('./serviceAccountKey.json')
adefualt_app = firebase_admin.initialize_app(cred, {'databaseURL': 'https://synagogue-81a73.firebaseio.com',
'databaseAuthVatiableOverride':{
    'uid':'work'
}})

menu_actions = {}


# Main menu
def main_menu():
    print("Please choose the menu you want to start:")
    print("1. Print prayers")
    print("2. Add prayer")
    print("3. Remove prayer")
    print("4. Make Gabay")
    print("\n0. Quit")
    choice = raw_input(" >> ")
    exec_menu(choice)

    return


def printPrayers():
    ref = db.reference('database').child('prayer').get()
    if(ref != None):
        arr = []
        for uid in ref.keys():
            arr.append(uid)
        i = 0
        for user in ref.values():
            num=(i+1)
            print ("User UID: " + arr[i])
            for key, val in user.items():
                print (key +": "+ str(val))
            i += 1
            print("\n")
    else:
        print ("DataBase is EMPTY!")
    main_menu()



def add():
    email = (raw_input("Enter email: "))
    if not re.match("[^@]+@[^@]+\.[^@]+", email):
        print ("Wrong Email format")
        add()
    password = (raw_input("Enter password: "))
    name = (raw_input("Enter full name: "))
    birthday = (raw_input("Enter birthday date: "))
    flag =True
    while (flag):
        try:
            datetime.datetime.strptime(birthday, '%d/%m/%Y')
            flag = False
        except ValueError:
            print ("Incorrect data format, should be DD/MM/YYYY")
            birthday = (raw_input("Enter birthday date: "))


    newUser = auth.create_user(email=email, password=password)
    ref = db.reference('database').child('prayer').child(newUser.uid)
    ref.set({
        'name': name,
        'email': email,
        'phone': '',
        'birthday': birthday,
        'address': '',
        'isGabay': False,
        'imageURL': ''
    })
    print (name + " added successfully \n")
    main_menu()

def remove():
    ref = db.reference('database').child('prayer').get()
    if ref != None :
        while (True):
            mailRemove = (raw_input("Choose email to delete:"))
            for uid in ref.keys():
                if db.reference('database').child('prayer').child(uid).child('email').get() == mailRemove:
                    auth.delete_user(uid)
                    db.reference('database').child('prayer').child(uid).delete()
                    if db.reference('database').child('gabay').child(uid).child('email').get() == mailRemove:
                        db.reference('database').child('gabay').child(uid).delete()
                    print(mailRemove + " removed \n")
                    main_menu()
                    return
            print ("can't find this mail")
    else:
        print ("Database is EMPTY!")
    main_menu()


def makeManager():
    ref = db.reference('database').child('prayer').get()
    if ref != None :
        while (True):
            mailManager = (raw_input("Choose email to make Gabay:"))
            for uid in ref.keys():
                if db.reference('database').child('prayer').child(uid).child('email').get() == mailManager:
                    db.reference('database').child('prayer').child(uid).child('isGabay').set(True)
                    print(db.reference('database').child('prayer').child(uid).child('name').get() + " is manager now! \n")
                    a = db.reference('database').child('gabay').child(uid).child('email').set(mailManager)

                    main_menu()
                    return
            print ("can't find this mail")
    else:
        print ("Database is EMPTY!")
    main_menu()



# Execute menu
def exec_menu(choice):
    ch = choice.lower()
    if ch == '':
        menu_actions['main_menu']()
    else:
        try:
            menu_actions[ch]()
        except KeyError:
            print("Invalid selection, please try again.\n")
            menu_actions['main_menu']()
    return


# Back to main menu
def back():
    menu_actions['main_menu']()


# Exit program
def exit():
    sys.exit()


# =======================
#    MENUS DEFINITIONS
# =======================

# Menu definition
menu_actions = {
    'main_menu': main_menu,
    '1': printPrayers,
    '2': add,
    '3': remove,
    '4': makeManager,
    '9': back,
    '0': exit,
}

# =======================
#      MAIN PROGRAM
# =======================
def makeadmin():
    newUser = auth.create_user(email='admin@app.com', password='App1234')
    ref = db.reference('database').child('admin').child(newUser.uid).set({ \
        'userName': 'admin', \
        'code': 'App1234'})
def signIn():
    ref = db.reference('database').child('admin').get()
    if ref != None:
        while (True):
            nameAdmin = (raw_input("Enter your username: "))
            passAdmin = (raw_input("Enter your password: "))
            for uid in ref.keys():
                if db.reference('database').child('admin').child(uid).child('userName').get() == nameAdmin:
                    if db.reference('database').child('admin').child(uid).child('code').get() == passAdmin:
                        print("conneted \n\nWelcome " + nameAdmin + ",\n")
                        main_menu()
                    else:
                        print ("Worng password")

            print("please Enter user and password again! \n")
    else:
        print("Database is EMPTY! \n I am making default Admin..")
        makeadmin()
        print("\n default Admin success!\n")
        signIn()
# Main Program
if __name__ == "__main__":
    # Launch main menu
    print("Welcome,\n")
    signIn()

#printUsers()
#add()
#remove()
