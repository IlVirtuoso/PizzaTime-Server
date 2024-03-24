#[derive(Serialize)]
#[serde(crate="rocket::serde")]
pub struct Jsonable{
    payload: String,
    message: String
}


#[post("/hello", data="<data>")]
pub fn hello(data: Form<String>) -> Json<Jsonable>{
    Json(Jsonable{payload:"hello".to_string(), message:"ciao".to_string()})
}
