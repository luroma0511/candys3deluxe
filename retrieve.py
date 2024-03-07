import os

class Jars():

    def __init__(self):
        self.files = self.list_files("jars")
    
    def list_files(self, directory):
        arr = []
        files = os.listdir(directory)
        for file in files:
            print(file)
            arr.append(file)
        return arr