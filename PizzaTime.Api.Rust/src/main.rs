use rocket::{fs::FileServer, futures::stream::Collect, Build, Rocket, Route};

mod data;
mod Controllers;

#[macro_use] extern crate rocket;
#[launch]
fn rocket() -> Rocket<Build> {
    
    rocket::build().mount("/", FileServer::from("./dist/"))
    .mount("/api", routes![])
}





