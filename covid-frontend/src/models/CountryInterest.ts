export default class CountryInterest {
    country_name: string;
    total_users: number;
    users_names: Array<String>;

    constructor(country?: CountryInterest){
        if(country){
            this.country_name = country.country_name;
            this.total_users = country.total_users;
            this.users_names = country.users_names;
        }else{
            this.country_name = "";
            this.total_users = 0;
            this.users_names = [];
        }
    }
}