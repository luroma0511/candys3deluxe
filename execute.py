from pathlib import Path
import os
import subprocess

def __execute__(version):
    path = "game/versions"
    command = ["java\\jre1.8.0_351\\bin\\java.exe", "-jar", os.path.join(path, version)]
    try:
        subprocess.run(command, check=True)
    except subprocess.CalledProcessError as e:
        print("Error:", e)