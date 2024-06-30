

export class Ingredient {
  public constructor(
    public id: string,
    public commonName: string,
    public description: string,
    public glutinFree: boolean,
    public allergen: string[] 
  ) { }
}

