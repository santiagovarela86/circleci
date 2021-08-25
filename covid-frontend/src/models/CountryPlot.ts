import Country from "./Country";
import StatisticsDay from "./StatisticsDay";

export default class CountryPlot{
    public country: Country;
    public statistics_day_list: Array<StatisticsDay>;

    constructor(countryPlot?: any){
        if(countryPlot){
            this.country = countryPlot.country;
            this.statistics_day_list = countryPlot.statistics_day_list;

        }else{
            this.country = new Country();
            this.statistics_day_list = new Array<StatisticsDay>();
        }
    }
}