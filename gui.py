import subprocess, json, time, sys, os, shutil, pyautogui
from datetime import datetime
from pyfiglet import Figlet

class Gui():

    def __init__(self):
        self.deployed = False

    def info(self):

        """

        This method initiates the first SPLASSH GUI, which will specify the
        UNI of the user as well as the name of the mouse for the trial.

        Once the GUI is closed, the settings.json file is updated, and both the
        mouse name and UNI are passed back to the main function, where they will
        be used to create a file path.

        """

        os.chdir("JSPLASSH")
        if os.path.isfile("settings.json"):
            os.remove("settings.json")
        subprocess.call(["java", "-jar", "info.jar"])
        os.chdir("..")

    def camera(self):

        """

        This method opens the GUI which will update the settings.json with the
        specified camera parameters.

        """

        cpu_name = os.environ['COMPUTERNAME']
        print("This computer's name is "+cpu_name)
        if cpu_name == "DESKTOP-TFJIITU":
            path = "S:/WFOM/data/"
        else:
            path = "C:/WFOM/data/"

        print("Setting Camera parameters...")
        os.chdir("JSPLASSH")
        subprocess.Popen(["java", "-jar","camera.jar"])
        os.chdir("..")

    def make_directories(self, settings):
        date = str(datetime.now())[:10]

        with open("JSPLASSH/archive.json", "r+") as f:
            archive = json.load(f)
            d = archive["mice"][mouse]["last_trial"]+1
            archive["mice"][mouse]["last_trial"] = d
            f.seek(0)
            json.dump(archive, f, indent=4)
            f.truncate()
        f.close()

        if not os.path.isdir(path + mouse + "_" + str(d)):
            path = path + mouse + "_" + str(d)
        else:
            path = path + mouse + "_" + str(d+1)

        print("Making directory: "+path)
        os.mkdir(path)
        print("Making directory: "+path+"/CCD")
        os.mkdir(path+"/CCD")
        print("Making directory: "+path+"/webcam")
        os.mkdir(path+"/webcam")

        print("Moving JSPLASSH/settings.json to "+path+"/settings.json")
        src = "JSPLASSH/settings.json"
        dst = path+"/settings.json"
        shutil.move(src, dst)
        return path

    def exit(self):

        """

        This method simply provides an exit script for the main function.

        """

        for i in [4,3,2,1]:
            print("Exiting in: ", sep=' ', end=': ')
            print(i, sep=' ', end='\r')
            time.sleep(1)
        sys.exit()

    def banner(self, text, font):

        """

        Cool and completely unnecessary banners.

        """

        custom_fig = Figlet(font=font, direction='auto', justify='auto', width=80)
        print(custom_fig.renderText(text))
