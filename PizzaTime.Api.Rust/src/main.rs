use rocket::{fs::FileServer, Build, Rocket, Route};

mod Data;


#[macro_use] extern crate rocket;
#[launch]
fn rocket() -> Rocket<Build> {
    rocket::build().mount("/", FileServer::from("./dist/"))
}



