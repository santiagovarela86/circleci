import React, { useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Button from "@material-ui/core/Button";
import RestProvider from "../../../../rest/RestProvider";
import CountryListStats from "../../../../models/CountryListsStats";
import { toast } from 'react-toastify';
import CircularProgress from "@material-ui/core/CircularProgress";
import RefreshIcon from '@material-ui/icons/Refresh'
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';

const useStyles = makeStyles({
    root: {
        width: '100%',
    },
    container: {
        maxHeight: 500,
    },
});

export default function ListReport() {
    const classes = useStyles();
    const [isLoading, setIsLoading] = React.useState(false)
    const [dailyStats, setDailyStats] = React.useState(new CountryListStats());
    const [threeDaysStats, setThreeDaysStats] = React.useState(new CountryListStats());
    const [weeklyStats, setWeeklyStats] = React.useState(new CountryListStats());
    const [monthlyStats, setMonthlyStats] = React.useState(new CountryListStats());
    const [initialStats, setInitialStats] = React.useState(new CountryListStats());

    useEffect(() => {
        refreshReports();
    }, [])

    const refreshReports = () => {
        setIsLoading(true);
        Promise.all([-1, 0, 3, 7, 30].map(dias => new RestProvider().getCountryListInfo(dias)))
            .then(countriesListStats => {
                countriesListStats.forEach(countryListStats => {
                    switch (countryListStats.last_days) {
                        case 0:
                            setDailyStats(countryListStats);
                            break;
                        case 3:
                            setThreeDaysStats(countryListStats);
                            break;
                        case 7:
                            setWeeklyStats(countryListStats);
                            break;
                        case 30:
                            setMonthlyStats(countryListStats);
                            break;
                        case -1:
                            setInitialStats(countryListStats);
                            break;
                        default:
                            break;
                    }
                })
            })
            .catch(err => {
                toast.error("Hubo un error al generar al reporte. Intente más tarde.")
            })
            .finally(() => {
                setIsLoading(false);
            })
    }

    return (
        <div>
            <div style={{ display: "flex" }}>
                <Typography className={classes.title} variant="h6" id="tableTitle" component="div">
                    Reporte de Listas
                    </Typography>
                <Button
                    style={{ marginLeft: "30px" }}
                    variant="contained"
                    color="primary"
                    onClick={refreshReports}
                    startIcon={isLoading ? <CircularProgress style={{ color: "white" }} size={20} /> : <RefreshIcon />}
                >
                    Refrescar
                </Button>
            </div>
            <div style={{ marginTop: "16px" }}>
                <TableContainer component={Paper}>
                    <Table aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell>Último Día</TableCell>
                                <TableCell>Últimos 3 Días</TableCell>
                                <TableCell>Últimos 7 Días</TableCell>
                                <TableCell>Últimos 30 Días</TableCell>
                                <TableCell>Desde Siempre</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            <TableRow>
                                <TableCell>{isLoading ? <CircularProgress style={{ color: "white" }} size={20} /> : dailyStats.count}</TableCell>
                                <TableCell>{isLoading ? <CircularProgress style={{ color: "white" }} size={20} /> : threeDaysStats.count}</TableCell>
                                <TableCell>{isLoading ? <CircularProgress style={{ color: "white" }} size={20} /> : weeklyStats.count}</TableCell>
                                <TableCell>{isLoading ? <CircularProgress style={{ color: "white" }} size={20} /> : monthlyStats.count}</TableCell>
                                <TableCell>{isLoading ? <CircularProgress style={{ color: "white" }} size={20} /> : initialStats.count}</TableCell>
                            </TableRow>
                        </TableBody>
                    </Table>
                </TableContainer>
            </div>
        </div>
    );
}