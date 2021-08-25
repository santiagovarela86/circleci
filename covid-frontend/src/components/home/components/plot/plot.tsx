import React, { useEffect, useState } from 'react';
import Typography from '@material-ui/core/Typography';
import {
    LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend,
} from 'recharts';
import { Select, MenuItem, CircularProgress } from '@material-ui/core';
import CountryList from '../../../../models/CountryList';
import User from '../../../../models/User';
import RestProvider from '../../../../rest/RestProvider';
import CountryListPlot from '../../../../models/CountryListPlot';
import CountryPlot from '../../../../models/CountryPlot';
import StatisticsDay from '../../../../models/StatisticsDay';
import moment from 'moment';

const colors = [
    '#ffc658',
    '#d0ed57',
    '#a4de6c',
    '#82ca9d',
    '#8dd1e1',
    '#83a6ed',
    '#8884d8'
]

const generateData = (plot: CountryListPlot, view: string) => {
    var result = new Array<any>();
    plot.country_plot_list = plot.country_plot_list.sort((a, b) => {
        if (a.country.name < b.country.name) { return -1; }
        if (a.country.name > b.country.name) { return 1; }
        return 0;
    })
    plot.country_plot_list.forEach((countryPlot: CountryPlot) => {
        countryPlot.statistics_day_list.forEach((statistic: StatisticsDay) => {
            var date = moment(statistic.date).format("DD/MM/YYYY")
            var day = result.find(data => data.name === date);
            if (!day) {
                day = { name: date, day: statistic.date }
                result.push(day);
            }
            day[countryPlot.country.name] = statistic[view];
        })
    })
    result = result.sort((a, b) => {
        return new Date(a.day).getTime() - new Date(b.day).getTime();
    })
    return result;
}

export default function Plot(props: { userLogged: User, selectedList: CountryList, changeSelectedList: (countryList: CountryList) => void, drawerOpen: boolean }) {

    const [countryLists, setCountryLists] = useState(new Array<CountryList>());
    const [plot, setPlot] = useState(new CountryListPlot());
    const [data, setData] = useState(new Array<any>());
    const [view, setView] = useState("confirmed");
    const [isLoading, setIsLoading] = useState(false);

    const selectedId = props.selectedList.id;
    const changeSelectedList = props.changeSelectedList;

    useEffect(() => {
        var rest = new RestProvider();
        rest.getCountryLists({})
            .then((countryLists: Array<CountryList>) => {
                setCountryLists(countryLists);
                if (selectedId === 0 && countryLists.length > 0) {
                    changeSelectedList(countryLists[0]);
                }
            })
            .catch((err: any) => {
                console.log(err);
            })
    }, [selectedId, changeSelectedList])

    useEffect(() => {
        setIsLoading(true);
        new RestProvider().getListPlot(props.selectedList.id)
            .then((plot) => {
                setPlot(plot);
            })
            .catch(() => {

            })
            .finally(() => {
                setIsLoading(false);
            })
    }, [props.selectedList.id])

    useEffect(() => {
        setData(generateData(plot, view));
    }, [plot, view])

    const handleChangeSelectedList = (event: any) => {
        var selectedCountryList = countryLists.find(countryList => countryList.id === event.target.value);
        if (selectedCountryList) {
            props.changeSelectedList(selectedCountryList);
        }
    }

    return (
        <div>
            <Typography variant="h6" id="tableTitle" component="div">
                Ploteos
            </Typography>
            <div style={{ display: "flex", alignItems: "center" }}>
                <Select
                    label="list"
                    value={props.selectedList.id}
                    onChange={handleChangeSelectedList}
                    disabled={isLoading}
                >
                    {countryLists.map(countryList => {
                        return (
                            <MenuItem key={countryList.id} value={countryList.id}>{countryList.name}</MenuItem>
                        )
                    })}
                </Select>
                <Select
                    style={{ marginLeft: "30px" }}
                    label="view"
                    value={view}
                    onChange={(event: any) => setView(event.target.value)}
                    disabled={isLoading}
                >
                    <MenuItem value={"confirmed"}>Confirmados</MenuItem>
                    <MenuItem value={"deaths"}>Muertos</MenuItem>
                    <MenuItem value={"recovered"}>Recuperados</MenuItem>
                </Select>
                {
                    isLoading &&
                    <CircularProgress
                        style={{ marginLeft: "30px" }}
                        size={20}
                    />
                }
            </div>
            <LineChart
                style={{ marginTop: "20px" }}
                width={window.innerWidth - 48 - (props.drawerOpen ? 300 : 0)}
                height={window.innerHeight - 200}
                data={data}
                margin={{
                    top: 5, right: 30, left: 20, bottom: 15,
                }}
            >
                <CartesianGrid strokeDasharray="0 0" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip />
                <Legend verticalAlign="top" height={25} />
                {plot.country_plot_list.map((countryPlot, index) => {
                    return (
                        <Line key={index} type="monotone" stroke={colors[index] ? colors[index] : '#' + Math.floor(Math.random() * 16777215).toString(16)} dataKey={countryPlot.country.name} />
                    )
                })}
            </LineChart>
        </div>
    )
}