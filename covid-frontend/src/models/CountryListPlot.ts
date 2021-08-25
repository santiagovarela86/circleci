import CountryPlot from './CountryPlot'

export default class CountryListPlot{
    public id: number;
    public name: string;
    public country_plot_list: Array<CountryPlot>;

    constructor(countryListPlot?: any){
        if(countryListPlot){
            this.id = countryListPlot.id;
            this.name = countryListPlot.name;
            this.country_plot_list = countryListPlot.country_plot_list;
        }else{
            this.id = 0;
            this.name = "";
            this.country_plot_list = new Array<CountryPlot>();
        }
    }
}