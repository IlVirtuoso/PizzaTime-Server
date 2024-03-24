use std::io::Write;
use std::process::Command;
use std::fs;

pub fn main(){
    writeln!(&mut std::io::stdout(), "Building SPA");
    build_spa();
    deploy_spa();
}


pub fn build_spa(){
    Command::new("npm").current_dir("../PizzaTimeSPA").args(["run","build"]);
}

pub fn deploy_spa(){

}