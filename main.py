import time, subprocess, os, datetime, json, sys, string
import pylablib as pll
from arduino import Arduino
from andor import Andor
from gui import Gui


if __name__ == '__main__':

    # Welcome Banner
    Gui.banner("WFOM", "isometric1")
    Gui.banner("WELCOME TO SPLASSH", "contessa")
    print("Testing Connection to the Arduino")
    Arduino.message_to_arduino("s")
    # Initiate the SPLASSH Gui
    uni, mouse = Gui.open_GUI()
    try:
        path = Andor.create_camera_file_folder(mouse)
    except FileExistsError as e:
        print(e.with_traceback)
        Gui.exit()
    Gui.solis_GUI()
    try:
        dst = Andor.deploy_settings(path)
    except FileNotFoundError as e:
        print(e.filename)
