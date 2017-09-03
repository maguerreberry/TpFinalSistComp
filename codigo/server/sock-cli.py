import socket
# from Padding.py import appendPadding, removePadding
import time
from Crypto.Cipher import AES
import padding as pad

block_size = 16

def do_encrypt(message):
    obj = AES.new('This is a key123', AES.MODE_CBC, 'This is an IV456')
    ciphertext = obj.encrypt(pad.appendPadding(message)) #Agrego padding para AES por defecto block size = 16 modo CMS aceptado por RFC
    return ciphertext

def do_decrypt(ciphertext):
    obj2 = AES.new('This is a key123', AES.MODE_CBC, 'This is an IV456')
    message = obj2.decrypt(ciphertext)
    return pad.removePadding(message).decode('ascii')


clientsocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# <<<<<<< HEAD

# def set_message()

if __name__ == '__main__':
	print('--Bienvenido a la app del porton (proximamente en Android)--')
	# print()
	pin_ss = 'aa'
	user_in = 0
	is_admin = 0
	user = ''
	password = ''

	while pin_ss != '1234':
		pin_ss = input('--Ingrese su pin super seguro de 4 digitos--')

	# clientsocket.connect(('192.168.0.14', 8089))
	try:
		clientsocket.connect(('localhost', 8089)) 
	except Exception as e: 
		print("Exception is %s" %  e)

	while 1:
		if user_in == 0:
			print('LOGIN - Ingrese su usuario y password para el acceso al sistema')
			new_username = input('Nombre de usuario')
			new_password = input('Password')

			message = '01-%s-%s' % (new_username,new_password) #Login de un nuevo usuario

			c_text = do_encrypt(message)
			clientsocket.send(c_text)

			login_ok = do_decrypt(clientsocket.recv(64))

			if login_ok == '2':
				is_admin = 1
				user_in = 1
				user = new_username
				password = new_password
			elif login_ok == '1':
				user_in = 1
				user = new_username
				password = new_password
			elif login_ok == '0':
				user_in = 0

			# ACA CHEQUEAR CON EL SERVIDOR QUE EL USUARIO Y LA PASS ESTEN BIEN

		if user_in == 1:
			print('INGRESO USUARIO \n --Elija una opcion -- \n1)Abrir porton \n2)Cerrar porton \n3)Cerrar sesion \n4)Ingreso administrador \n5)Salir\n')
			op = input('>')
			if op == '1':
				message = '11-%s-%s' % (user,password)

				c_text = do_encrypt(message)
				clientsocket.send(c_text)

				print('Abriendo porton')
			elif op == '2':
				message = '12-%s-%s' % (user,password)

				c_text = do_encrypt(message)
				clientsocket.send(c_text)

				print('Cerrando porton')
			elif op == '3':
				user_in = 0 
			elif op == '4':
				if is_admin == 0:
				 	print('Ud no es administrador')
				else:
					user_in = 2
			elif op == '5':
				print('Saliendo')
				break

		elif user_in == 2 & is_admin == 1:
			print('INGRESO ADMINISTRADOR\n-- Administracion de usuarios\n 1)Ingreso nuevo usuario\n 2)Eliminar usuario\n 3)Salir modo administrador \n 4)Salir')
			op = input('>')
			if op == '3':
				user_in = 1
			elif op == '4':
				print('Saliendo')
				break
			elif op == 1:
				print('NUEVO USUARIO - Ingrese su usuario y password para el acceso al sistema')
				new_username = input('Nombre de usuario')
				new_password = input('Password')

				message = '21-%s-%s' % (new_username,new_password) #Set nuevo usuario

				c_text = do_encrypt(message)
				clientsocket.send(c_text)
			elif op == 2:
				del_username = input('Nombre de usuario a borrar')
				message = '22-%s-%s' % (new_username,new_password) #Ingreso usario a borrar
				c_text = do_encrypt(message)
				clientsocket.send(c_text)




				# print('Ingreso nuevo usuario')
				# new_username = 




# # 	op = input('>')

# =======
# clientsocket.connect(('localhost', 8089))
# text = input('Ingrese Comando\n')
# c_text = do_encrypt('nati-nati-' + text)
# clientsocket.send(c_text)
# >>>>>>> 4747a815f7fd0255d9c49ba830a2d4bee5c50b76
