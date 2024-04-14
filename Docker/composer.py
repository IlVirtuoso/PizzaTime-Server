import argparse
import os 

args = argparse.ArgumentParser()
args.add_argument('-u', '--up', help='tell the script to build the docker image', required= False, action='store_true')
args.add_argument('-d', '--down', help='tell the script to stop and remove the container' ,required= False , action='store_true')
args.add_argument('-r','--remove', help='tell the script to remove the image', required= False , action='store_true')
args = args.parse_args()
os.chdir(os.path.dirname(__file__))
if args.up:
    os.system("podman-compose up -d")
elif args.down:
    os.system("podman-compose down")
    pass

if args.remove:
    os.system("podman image rm pizzatimeservice")
    os.system("podman image rm pizzatimedatabase")
    pass


