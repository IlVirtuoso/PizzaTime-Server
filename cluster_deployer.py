#!bin/python3

import argparse
import pathlib
import os 
from time import sleep
cwd = pathlib.Path(__file__).parent.absolute()
parser = argparse.ArgumentParser()


parser.add_argument("-u", action="store_true"),
parser.add_argument("-d", action="store_true")


parser.add_argument("--build-images", action="store_true"),
parser.add_argument("--remove-images", action="store_true")


args = parser.parse_args()
print(cwd)
os.chdir(cwd)
projects = ["PizzaTime.Gateway", "PizzaTime.PizzaEngine", "PizzaTime.OrderService", "PizzaTime.pizzaidph2"]
cwp = str(cwd)

def build_images():
    for proj in projects:
        os.chdir(cwp + "/" + proj)
        print("Building image for  project {}".format(proj))
        os.system("minikube image build . -t {}".format(proj.lower()))
        os.chdir(cwp)
        pass
    pass
pass

def remove_images():
    for proj in projects:
        print("Removing image for project {}".format(proj))
        os.system("minikube image rm {}".format(proj.lower()))
        pass
    pass
pass





yaml = ["PersistentVolumes.yaml","RabbitMqPod.yaml", "PizzaEnginePod.yaml", "PizzaGateway.yaml","PizzaIdpPod.yaml", "PizzaOrderService.yaml"]

def deploy():
    if args.build_images:
        build_images()
        
    pass
    def apply(fileName):
        os.system("kubectl apply -f ./{}".format(fileName))
        sleep(1)
        pass
    os.chdir(cwp + "/Kubernetes")
    for y in yaml:
        apply(y)
        pass
    os.chdir(cwp)
    pass
pass 

def undeploy():
    def delete(fileName):
        os.system("kubectl delete -f ./{}".format(fileName))
        pass
    os.chdir(cwp + "/Kubernetes")
    for y in yaml:
        delete(y)
        pass
    if args.remove_images:
        remove_images()
        pass
    os.chdir(cwd)
    pass
pass


if args.u:
    deploy()
    pass
elif args.d:
    undeploy()
pass 