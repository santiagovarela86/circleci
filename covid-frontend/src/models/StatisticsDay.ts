export default class StatisticsDay {
    public date: Date;
    public confirmed: number;
    public deaths: number;
    public recovered: number;

    [key: string]: any;

    constructor(statisticsDay?: any) {
        if (statisticsDay) {
            this.date = statisticsDay.date;
            this.confirmed = statisticsDay.confirmed;
            this.deaths = statisticsDay.deaths;
            this.recovered = statisticsDay.recovered;
        } else {
            this.date = new Date();
            this.confirmed = 0;
            this.deaths = 0;
            this.recovered = 0;
        }
    }
}