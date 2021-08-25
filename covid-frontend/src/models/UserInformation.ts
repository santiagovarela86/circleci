export default class UserInformation{
    public id: number;
    public user_name: string;
    public telegram_id: string;
    public name: string;
    public last_name: string;
    public email: string;
    public total_lists: number;
    public total_countries: number;
    public last_login: string;

    constructor(userInformation?: any){
        if(userInformation){
            this.id = userInformation.id;
            this.user_name = userInformation.user_name;
            this.name = userInformation.name;
            this.last_name = userInformation.last_name;
            this.email = userInformation.email;
            this.last_login = userInformation.last_login;
            this.telegram_id = userInformation.telegram_id;
            this.total_lists = userInformation.total_lists;
            this.total_countries = userInformation.total_countries;
        }else{
            this.id = 0;
            this.user_name = "";
            this.name = "";
            this.last_name = "";
            this.email = "";
            this.last_login = "";
            this.telegram_id = "";
            this.total_lists = 0;
            this.total_countries = 0;
        }
    }
}