import Country from "./Country";

export default class CountryList{
    public id: number;
    public name: string;
    public countries: Array<Country>;
    public user_id: number;
    public creation_date: String;

    constructor(countryList?: any){
        if(countryList){
            this.id = countryList.id;
            this.name = countryList.name;
            this.countries = countryList.countries.map((country: any) => {return new Country(country)});
            this.user_id = countryList.user_id;
            this.creation_date = countryList.creation_date;
        }else{
            this.id = 0;
            this.name = "";
            this.countries = [];
            this.user_id = 0;
            this.creation_date = "";
        }
    }
}