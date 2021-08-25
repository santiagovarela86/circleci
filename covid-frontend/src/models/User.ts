export default class User{
    public id: number;
    public user_name: string;
    public name: string;
    public last_name: string;
    public email: string;
    public last_login: string;

    constructor(user?: any){
        if(user){
            this.id = user.id;
            this.user_name = user.user_name;
            this.name = user.name;
            this.last_name = user.last_name;
            this.email = user.email;
            this.last_login = user.last_login;
        }else{
            this.id = 0;
            this.user_name = "";
            this.name = "";
            this.last_name = "";
            this.email = "";
            this.last_login = "";
        }
    }
}