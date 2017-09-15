import base64
from Crypto.Cipher import AES
import padding as pad

block_size = 16
iv = b'RandomInitVector'
key = 'Bar12345Bar12345'

def do_encrypt(message):
    obj = AES.new(key, AES.MODE_CBC, iv)
    ciphertext = obj.encrypt(pad.appendPadding(message)) #Agrego padding para AES por defecto block size = 16 modo CMS aceptado por RFC
    return base64.b64encode(ciphertext).decode('utf-8')

def do_decrypt(ciphertext):
    obj2 = AES.new(key, AES.MODE_CBC, iv)
    message = obj2.decrypt(base64.b64decode(ciphertext))
    return pad.removePadding(message).decode('utf-8')

enc_mess = do_encrypt('Mensaje a encriptar')
print (enc_mess)

dec_message = do_decrypt('Mensaje a desencriptar')
print (dec_message)
