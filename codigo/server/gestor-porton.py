import socket
import RPi.GPIO as gpio
import time
import sys
from usuario import *
from encriptador import *
import xml.etree.ElementTree as xml

def new_user(users_list, name, pwd, pl):

    for user in users_list:
        if(user.get_user_name() == name):
            return 22

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

    return 0

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
        if(22 == new_user(users_list, admin_name, admin_pwd, "0")):
            print("ERROR! User exists.")
            exit(-1)

    else:
        print("ERROR! Invalid CLI Command! Use '--config' option to create admin user.")
        exit(-1)

cifrador = Encriptador()

while True:    
    connection, address = serversocket.accept()
    
    data = [""]
    while len(data) < 3:
        buf = connection.recv(64)            
        #buf = cifrador.do_decrypt(buf)
        data = buf.decode()
        data = data.split('-')
        
    client_user = data[0]
    client_pwd = data[1]
    data[2] = data[2].split('\n')
    client_cmd = data[2][0]

    print('client_user |%s|' %client_user, 'client_pwd |%s|' %client_pwd, 'client_cmd |%s|' %client_cmd)    
    usuario_cliente = Usuario("nada", "nada", "nada") 

    for user_object in users_list:
        if(str(user_object.get_user_name()) == client_user):
            if (str(user_object.get_user_pwd()) == client_pwd):
                usuario_cliente = user_object
                client_pl = user_object.get_user_pl()   
        
    if(usuario_cliente.get_user_name() == "nada"):
        connection.send(cifrador.do_encrypt("Incorrecto").encode())   
    
    else:
        if client_cmd == 'new_user':
            if client_pl == "0":                    
                data[4] = data[4].split('\n')
                if new_user(users_list, data[3], data[4][0], "1") == 22:
                    connection.send(cifrador.do_encrypt("existente").encode())
                else:                       
                    connection.send(cifrador.do_encrypt("okay").encode())   
                
        elif client_cmd == 'abrir':
            #gpio.output(16,True)
            #gpio.output(18,False)
            connection.send(cifrador.do_encrypt("abriendo").encode())   
            #time.sleep(15)
            #gpio.output(16,False)
            pass

        elif client_cmd == 'cerrar':
            #gpio.output(18,True)
            #gpio.output(16,False)
            connection.send(cifrador.do_encrypt("cerrando").encode())   
            #time.sleep(12)
            #gpio.output(18,False)
            pass

        elif client_cmd == 'login':
            connection.send(cifrador.do_encrypt(client_pl).encode())   

        else:
            print('Comando incorrecto')

    connection.close()
