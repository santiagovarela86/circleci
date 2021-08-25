export type CountryData = {
    id: number;
    name: string;
    iso_country_code: string;
    start_date: Date;
    offset: number;
    strategy:string;
}

export default class Country {
    id: number;
    name: string;
    iso_country_code: string;
    start_date: Date;
    offset: number;
    strategy:string;

    constructor(country?: CountryData){
        if(country){
            this.id = country.id;
            this.name = country.name;
            this.iso_country_code = country.iso_country_code;
            this.start_date = country.start_date;
            this.offset = country.offset;
            this.strategy = country.strategy;
        }else{
            this.id = 0;
            this.name = "";
            this.iso_country_code = "";
            this.start_date = new Date();
            this.offset = 0;
            this.strategy = "";
        }
    }
}