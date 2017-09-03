import socket
# import RPi.GPIO as gpio
import time
import sys
import padding as pad
from Crypto.Cipher import AES
from usuario import *
import xml.etree.ElementTree as xml


def do_decrypt(ciphertext):
    obj2 = AES.new('This is a key123', AES.MODE_CBC, 'This is an IV456')
    message = obj2.decrypt(ciphertext)
    return pad.removePadding(message)

# gpio.setmode(gpio.BOARD)
# gpio.setup(12,gpio.OUT)
serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
serversocket.bind(('localhost', 8089))
serversocket.listen(5) # become a server socket, maximum 5 connections

users_list = []

xfile_tree = xml.parse('users.xml')
xfile = xfile_tree.getroot()

try:
    if sys.argv[1] == "--config":
        admin_name = input('Ingrese Nombre del admin\n')
        admin_pwd = input('Ingrese Pass del admin\n')
        new_admin = Usuario(admin_name, admin_pwd)
        new_user = xml.SubElement(xfile,'user')
        new_name = xml.SubElement(new_user, 'name')
        new_name.text = admin_name
        new_pwd = xml.SubElement(new_user, 'pwd')
        new_pwd.text = admin_pwd
        xfile_tree.write('users.xml')
except:
    pass
    

for user in xfile:
    users_list.append(Usuario(user[0].text, user[1].text))

for ii in range(len(users_list)):
    print ("name:", users_list[ii].get_user_name())
    print ("pwd:",users_list[ii].get_user_pwd())

while True:
    connection, address = serversocket.accept()
    buf = connection.recv(64)
    
    if len(buf) <= 0:
        print ("ERROR! Buffer empty!")
        exit(-1)

    buf = do_decrypt(buf)
    data = str(buf)
    data = data[2:len(data)-1]
    data = data.split('-')
    
    # print(str(buf))
    # print(data[2])

    if len(data) != 3:
        print('Datos Incorrectos')
        exit(-1)
    
    user = data[0]
    pwd = data[1]
    cmd = data[2]
    
    if True:
        pass    
    for ii in range(len(users)):
        
        pass

    if cmd == 'ledON':
    	print('LED ON')
    	gpio.output(12,True)
    elif cmd == 'ledOFF':
    	print('LED OFF')
    	gpio.output(12,False)
    elif cmd == 'exit':
    	print('chau')
    	serversocket.close()
    	break
    else:
    	print('Comando incorrecto')

    #serversocket.close()
    #break

