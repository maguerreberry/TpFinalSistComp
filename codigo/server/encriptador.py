import base64
from Crypto.Cipher import AES
import padding as pad

class Encriptador():

	def __init__(self):
		self.iv = b'RandomInitVector'
		self.key = 'Bar12345Bar12345'		

	def do_encrypt(self, message):
		obj = AES.new(self.key, AES.MODE_CBC, self.iv)
		ciphertext = obj.encrypt(pad.appendPadding(message)) #Agrego padding para AES por defecto block size = 16 modo CMS aceptado por RFC
		return base64.b64encode(ciphertext).decode('utf-8')

	def do_decrypt(self, ciphertext):
		obj2 = AES.new(self.key, AES.MODE_CBC, self.iv)
		message = obj2.decrypt(base64.b64decode(ciphertext))
		return pad.removePadding(message).decode('utf-8')