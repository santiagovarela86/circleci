import Country from '../models/Country'
import CountryList from "../models/CountryList";
import { CountryData } from '../models/Country';
import CountryListPlot from '../models/CountryListPlot';
import User from '../models/User';
import { toast } from 'react-toastify';
import UserInformation from "../models/UserInformation";
import CountryInterest from "../models/CountryInterest";
import CountryListStats from "../models/CountryListsStats";

var $ = require("jquery");

export default class RestProvider {

    public static token: string;

    public isAdmin(options: {}) {
        // @ts-ignore
        let url: string = process.env.REACT_APP_API_ADDR;
        return new Promise<String>(function (resolve, reject) {
            $.ajax({
                url: url + "/isAdmin",
                method: "GET",
                ignoreClientCertificate:true, 
                headers: {
                    "Authorization": "Bearer " + RestProvider.token,
                    "accept": "application/json",
                },
                success: function (data: String) {
                    return resolve(data);
                },
                error: function (err: any) {
                    return reject(err);
                }
            })
        })
    }

    public getCountryListInfo(d: number) {
        // @ts-ignore
        let url: string = process.env.REACT_APP_API_ADDR;
        return new Promise<CountryListStats>(function (resolve, reject) {
            $.ajax({
                url: url + "/list-info?d=" + d,
                method: "GET",
                ignoreClientCertificate:true, 
                headers: {
                    "Authorization": "Bearer " + RestProvider.token,
                    "accept": "application/json",
                },
                success: function (data: CountryListStats) {
                    return resolve(data);
                },
                error: function (err: any) {
                    return reject(err);
                }
            })
        })
    }

    public getCountries(options: {}) {
        // @ts-ignore
        let url: string = process.env.REACT_APP_API_ADDR;
        return new Promise<Array<Country>>(function (resolve, reject) {
            $.ajax({
                url: url + "/countries",
                method: "GET",
                ignoreClientCertificate:true, 
                headers: {
                    "Authorization": "Bearer " + RestProvider.token,
                    "accept": "application/json",
                },
                success: function (data: Array<CountryData>) {
                    return resolve(data.sort((a, b) => a.name.localeCompare(b.name)).map((country: any) => { return new Country(country) }));
                },
                error: function (err: any) {
                    return reject(err);
                }
            })
        })
    }

    public getCountryLists(options: {}) {
        // @ts-ignore
        let url: string = process.env.REACT_APP_API_ADDR;
        return new Promise<Array<CountryList>>(function (resolve: any, reject: any) {
            $.ajax({
                url: url + "/users/country-lists",
                method: "GET",
                ignoreClientCertificate:true, 
                headers: {
                    "Authorization": "Bearer " + RestProvider.token,
                    "accept": "application/json",
                },
                success: function (data: any) {
                    return resolve(data.map((countryList: any) => { return new CountryList(countryList) }));
                },
                error: function (err: any) {
                    return reject(err);
                }
            })
        })
    }

    public deleteCountryList(countryList: CountryList) {
        // @ts-ignore
        let url: string = process.env.REACT_APP_API_ADDR;
        return new Promise(function (resolve: any, reject: any) {
            $.ajax({
                url: url + '/country-lists/' + countryList.id,
                method: 'DELETE',
                ignoreClientCertificate:true, 
                headers: {
                    "Authorization": "Bearer " + RestProvider.token,
                    "accept": "application/json",
                },
                success: function (data: any) {
                    return resolve(data);
                },
                error: function (err: any) {
                    return reject(err);
                }
            })
        })
    }

    public updateCountryList(countryList: CountryList) {
        // @ts-ignore
        let url: string = process.env.REACT_APP_API_ADDR;
        return new Promise(function (resolve: any, reject: any) {
            $.ajax({
                url: url + '/country-lists/' + countryList.id,
                method: 'PUT',
                ignoreClientCertificate:true, 
                data: JSON.stringify(countryList),
                headers: {
                    "accept": "application/json",
                    "Authorization": "Bearer " + RestProvider.token,
                },
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                success: function (data: any) {
                    toast.success("La lista se ha actualizado exitosamente");
                    return resolve(data);
                },
                error: function (err: any) {
                    //bad request
                    if (err.status === 409){
                        toast.error("Revise que NO exista otra lista con el mismo nombre.");
                        return reject(err);
                    }else{
                        toast.error("Hubo un problema al guardar los cambios, intente más tarde.");
                        return reject(err);
                    }
                }
            })
        });
    }

    public getAllCountryLists(options: {}) {
        // @ts-ignore
        let url: string = process.env.REACT_APP_API_ADDR;
        return new Promise<Array<CountryList>>(function (resolve: any, reject: any) {
            $.ajax({
                url: url + "/country-lists",
                method: "GET",
                ignoreClientCertificate:true, 
                headers: {
                    "Authorization": "Bearer " + RestProvider.token,
                    "accept": "application/json",
                },
                success: function (data: any) {
                    return resolve(data.map((countryList: any) => { return new CountryList(countryList) }));
                },
                error: function (err: any) {
                    return reject(err);
                }
            })
        })
    }

    public login(loginOptions: { user_name: string, password: string }) {
        // @ts-ignore
        let url: string = process.env.REACT_APP_API_ADDR;
        return new Promise(function (resolve: any, reject: any) {
            $.ajax({
                url: url + '/login',
                method: 'POST',
                ignoreClientCertificate:true, 
                data: JSON.stringify(loginOptions),
                headers: {
                    "accept": "application/json",
                },
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                success: function (data: any) {
                    return resolve(data);
                },
                error: function (err: any) {
                    return reject(err);
                }
            })
        });
    }

    public register(registerOptions: { user_name: string, password: string, name: string, last_name: string, email: string }) {
        // @ts-ignore
        let url: string = process.env.REACT_APP_API_ADDR;
        return new Promise(function (resolve: any, reject: any) {
            $.ajax({
                url: url + '/sign-up',
                method: 'POST',
                ignoreClientCertificate:true, 
                data: JSON.stringify(registerOptions),
                headers: {
                    "accept": "application/json",
                },
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                success: function (data: any) {
                    return resolve(data);
                },
                error: function (err: any) {
                    return reject(err);
                }
            })
        });
    }

    public getListPlot(id: number) {
        // @ts-ignore
        let url: string = process.env.REACT_APP_API_ADDR;
        return new Promise<CountryListPlot>(function (resolve: any, reject: any) {
            $.ajax({
                url: url + '/country-lists/' + id + '/plot?d=1',
                method: 'GET',
                ignoreClientCertificate:true, 
                headers: {
                    "accept": "application/json",
                    "Authorization": "Bearer " + RestProvider.token,
                },
                success: function (data: CountryListPlot) {
                    return resolve(data);
                },
                error: function (err: any) {
                    return reject(err);
                }
            })
        });
    }

    public getUser(options: {}) {
        // @ts-ignore
        let url: string = process.env.REACT_APP_API_ADDR;
        return new Promise<User>(function (resolve: any, reject: any) {
            $.ajax({
                url: url + '/users',
                method: 'GET',
                ignoreClientCertificate:true, 
                headers: {
                    "accept": "application/json",
                    "Authorization": "Bearer " + RestProvider.token,
                },  
                success: function (data: User) {
                    return resolve(data);
                },
                error: function (err: any) {
                    return reject(err);
                }
            })
        });
    }

    public getCountryInterest(id: number) {
        // @ts-ignore
        let url: string = process.env.REACT_APP_API_ADDR;
        return new Promise<CountryInterest>(function (resolve: any, reject: any) {
            $.ajax({
                url: url + '/country-interest/' + id,
                method: 'GET',
                ignoreClientCertificate:true, 
                headers: {
                    "accept": "application/json",
                    "Authorization": "Bearer " + RestProvider.token,
                },
                success: function (data: CountryInterest) {
                    return resolve(data);
                },
                error: function (err: any) {
                    return reject(err);
                }
            })
        });
    }

    public getCountryId(id: number) {
        // @ts-ignore
        let url: string = process.env.REACT_APP_API_ADDR;
        return new Promise<Country>(function (resolve: any, reject: any) {
            $.ajax({
                url: url + '/countries/' + id,
                method: 'GET',
                ignoreClientCertificate:true, 
                headers: {
                    "accept": "application/json",
                    "Authorization": "Bearer " + RestProvider.token,
                },
                success: function (data: CountryInterest) {
                    return resolve(data);
                },
                error: function (err: any) {
                    return reject(err);
                }
            })
        });
    }

    public updateCountry(country: Country) {
        // @ts-ignore
        let url: string = process.env.REACT_APP_API_ADDR;
        return new Promise(function (resolve: any, reject: any) {
            $.ajax({
                url: url + '/countries/' + country.id,
                method: 'PUT',
                ignoreClientCertificate:true, 
                data: JSON.stringify(country),
                headers: {
                    "accept": "application/json",
                    "Authorization": "Bearer " + RestProvider.token,
                },
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                success: function (data: any) {
                    toast.success("El pais se actualizó exitosamente");
                    return resolve(data);
                },
                error: function (err: any) {
                    //bad request
                    if (err.status === 409){
                        toast.error("NO existe el pais indicado.");
                        return reject(err);
                    }else{
                        toast.error("Hubo un problema al guardar los cambios, intente más tarde.");
                        return reject(err);   
                    }
                }
            })
        });
    }

    public getMyUser(options: {}) {
        // @ts-ignore
        let url: string = process.env.REACT_APP_API_ADDR;
        return new Promise<Array<User>>(function (resolve: any, reject: any) {
            $.ajax({
                url: url + '/users',
                method: 'GET',
                ignoreClientCertificate:true, 
                headers: {
                    "accept": "application/json",
                    "Authorization": "Bearer " + RestProvider.token,
                },
                success: function (data: User) {
                    let array = [];
                    array.push(data);
                    return resolve(array);
                },
                error: function (err: any) {
                    return reject(err);
                }
            })
        });
    }

    public getAllUsers(options: {}) {
        // @ts-ignore
        let url: string = process.env.REACT_APP_API_ADDR;
        return new Promise<Array<User>>(function (resolve: any, reject: any) {
            $.ajax({
                url: url + '/users/all',
                method: 'GET',
                ignoreClientCertificate:true, 
                headers: {
                    "accept": "application/json",
                    "Authorization": "Bearer " + RestProvider.token,
                },
                success: function (data: Array<User>) {
                    return resolve(data);
                },
                error: function (err: any) {
                    return reject(err);
                }
            })
        });
    }

    public getUserInformation( id: number ) {
        // @ts-ignore
        let url: string = process.env.REACT_APP_API_ADDR;
        return new Promise<User>(function (resolve: any, reject: any) {
            $.ajax({
                url: url + '/user-information/' + id,
                method: 'GET',
                ignoreClientCertificate:true, 
                headers: {
                    "accept": "application/json",
                    "Authorization": "Bearer " + RestProvider.token,
                },
                success: function (data: Array<UserInformation>) {
                    return resolve(data);
                },
                error: function (err: any) {
                    return reject(err);
                }
            })
        });
    }

    public listsCompare( id1: number, id2: number ) {
        // @ts-ignore
        let url: string = process.env.REACT_APP_API_ADDR;
        return new Promise<Array<Country>>(function (resolve: any, reject: any) {
            $.ajax({
                url: url + `/list-compare?id1=${id1}&id2=${id2}`,
                method: 'GET',
                ignoreClientCertificate:true, 
                headers: {
                    "accept": "application/json",
                    "Authorization": "Bearer " + RestProvider.token,
                },
                success: function (data: Array<Country>) {
                    return resolve(data);
                },
                error: function (err: any) {
                    return reject(err);
                }
            })
        });
    }

    public createCountryList(selectedList: CountryList) {
        // @ts-ignore
        let url: string = process.env.REACT_APP_API_ADDR;
        return new Promise(function (resolve: any, reject: any) {
            $.ajax({
                url: url + '/country-lists/',
                method: 'POST',
                ignoreClientCertificate:true, 
                data: JSON.stringify(selectedList),
                headers: {
                    "accept": "application/json",
                    "Authorization": "Bearer " + RestProvider.token,
                },
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                success: function (data: any) {
                    toast.success("La lista se ha creado exitosamente");
                    return resolve(data);
                },
                error: function (err: any) {
                    //bad request
                    if (err.status === 409){
                        toast.error("Revise que NO exista otra lista con el mismo nombre.");
                        return reject(err);
                    }else{
                        toast.error("Hubo un problema al guardar los cambios, intente más tarde.");
                        return reject(err);
                    }
                }
            })
        });
    }

    public getCountryListsPlot(countryList: CountryList) {
        // @ts-ignore
        let url: string = process.env.REACT_APP_API_ADDR;
        return new Promise<CountryListPlot>(function (resolve: any, reject: any) {
            $.ajax({
                url: url + '/country-lists/plot',
                method: 'POST',
                ignoreClientCertificate:true, 
                data: JSON.stringify(countryList),
                headers: {
                    "Authorization": "Bearer " + RestProvider.token,
                    "accept": "application/json",
                },
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                success: function (data: CountryListPlot) {
                    return resolve(data);
                },
                error: function (err: any) {
                    return reject(err);
                }
            })
        });
    }
}