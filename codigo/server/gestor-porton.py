import socket
import RPi.GPIO as gpio
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

def new_user(users_list, name, pwd, pl):

    for user in users_list:
        if(user.get_user_name() == name):
            print("ERROR! User '%s' already exists!" %name)
            exit(-1)

    xfile_tree = xml.parse('users.xml')
    xfile_root = xfile_tree.getroot()

    new_user = xml.SubElement(xfile_root,'user')
    new_name = xml.SubElement(new_user, 'name')
    new_name.text = name
    new_pwd = xml.SubElement(new_user, 'pwd')
    new_pwd.text = pwd
    new_pl = xml.SubElement(new_user, 'pl')
    new_pl.text = pl
    xfile_tree.write('users.xml')

    users_list.append(Usuario(name, pwd, pl))

gpio.setmode(gpio.BOARD)
gpio.setup(16,gpio.OUT)
gpio.setup(18,gpio.OUT)
gpio.output(16,False)
gpio.output(18,False)

serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
serversocket.bind(('192.168.0.12', 7000))
serversocket.listen(5) # become a server socket, maximum 5 connections

users_list = []

xfile_tree = xml.parse('users.xml')
xfile_root = xfile_tree.getroot()

for user in xfile_root:
    users_list.append(Usuario(user[0].text, user[1].text, user[2].text))

if(len(sys.argv) > 1):
    if sys.argv[1] == "--config":
        admin_name = input('Ingrese nombre del admin\n')
        admin_pwd = input('Ingrese clave del admin\n')                
        new_user(users_list, admin_name, admin_pwd, "0")
    else:
        print("ERROR! Invalid CLI Command! Use '--config' option to create admin user.")
        exit(-1)

# for ii in range(len(users_list)):
#     print ("name:", users_list[ii].get_user_name())
#     print ("pwd:",users_list[ii].get_user_pwd())

connection, address = serversocket.accept()

while True:
    buf = connection.recv(64)
    
    if len(buf) <= 0:
        print ("ERROR! Buffer empty!")
        exit(-1)

    # buf = do_decrypt(buf)
    data = buf.decode()
    data = data.split('-')
    
    if len(data) != 3:
        print('Datos Incorrectos')
        exit(-1)
    
    client_user = data[0]
    client_pwd = data[1]
    data[2] = data[2].split('\r\n')
    client_cmd = data[2][0]

    print('client_user |%s|' %client_user, 'client_pwd |%s|' %client_pwd, 'client_cmd |%s|' %client_cmd)
    
    usuario_cliente = Usuario("nada", "nada", "nada") 

    for user_object in users_list:
        if(str(user_object.get_user_name()) == client_user):
            if (str(user_object.get_user_pwd()) == client_pwd):
                usuario_cliente = user_object
                # connection.send("Usuario Autenticado".encode())   
                connection.send(user_object.get_user_pl().encode())   
    
    if(usuario_cliente.get_user_name() == "nada"):
        connection.send("Usuario o clave incorrecta".encode())   
    
    else:
        if client_cmd == 'abrir':
            gpio.output(16,True)
            gpio.output(18,False)
            time.sleep(15)
            gpio.output(16,False)

        elif client_cmd == 'cerrar':
            gpio.output(18,True)
            gpio.output(16,False)
            time.sleep(10)
            gpio.output(18,False)

        elif client_cmd == 'exit':
        	print('chau')
        	serversocket.close()
        	break
        else:
        	print('Comando incorrecto')

    #serversocket.close()
    #break