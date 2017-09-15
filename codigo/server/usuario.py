import xml.etree.ElementTree as xml

class Usuario():

	def __init__(self, name, pwd, pl):
		self.user_name = name
		self.user_pwd = pwd
		self.user_pl = pl
		
	def get_user_name(self):
		return self.user_name

	def get_user_pwd(self):
		return self.user_pwd

	def get_user_pl(self):
		return self.user_pl

	def set_user_pl(self):
		return self.user_pl

	def set_user_name(self, value):
		self.user_name		

	def set_user_pwd(self, value):
		self.user_pwd		
