export default class CountryTag {
    id:number;
    country_name: string;
    strategy: string;
    constructor(country?: CountryTag){
        if(country){
            this.id =country.id;
            this.country_name = country.country_name;
            this.strategy = country.strategy;
        }else{
            this.id = 0;
            this.country_name = "";
            this.strategy = "";
        }
    }
}