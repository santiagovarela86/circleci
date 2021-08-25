export default class CountryListStats{
    public last_days: number;
    public start_date: string;
    public end_date: string;
    public count: number;

    constructor(countryListStats?: CountryListStats){
        if(countryListStats){
            this.last_days = countryListStats.last_days;
            this.start_date = countryListStats.start_date;
            this.end_date = countryListStats.end_date;
            this.count = countryListStats.count;
        }else{
            this.last_days = 0;
            this.start_date = "";
            this.end_date = "";
            this.count = 0;
        }
    }
}