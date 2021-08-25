import React, {useMemo} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TablePagination from '@material-ui/core/TablePagination';
import TableRow from '@material-ui/core/TableRow';
import Typography from '@material-ui/core/Typography';
import RestProvider from "../../../../rest/RestProvider";
import useGrid from "../../../../hooks/useGrid";
import CircularProgress from "@material-ui/core/CircularProgress";
import Tooltip from "@material-ui/core/Tooltip";
import IconButton from "@material-ui/core/IconButton";
import VisibilityIcon from "@material-ui/icons/Visibility";
import InteresPaisDialog from "./components/countryInterestDialog";
import CountryInterest from "../../../../models/CountryInterest";

const columns = [
    {id: 'name', label: 'Nombre', minWidth: 100},
    {id: 'iso_country_code', label: 'Código ISO', minWidth: 100},
    {
        id: 'actions',
        label: 'Ver Interés',
        minWidth: 170,
        align: "center"
    }
];

const useStyles = makeStyles({
    root: {
        width: '100%',
    },
    container: {
        maxHeight: 500,
    },
});

const getCountries = new RestProvider().getCountries

export default function CountryInterestGrid(props) {
    const classes = useStyles();
    const [page, setPage] = React.useState(0);
    const [rowsPerPage, setRowsPerPage] = React.useState(10);
    const options = useMemo(() => ({}), [])
    const [rows, isLoading, error] = useGrid(getCountries, options);
    const [countryInterest, setCountryInterest] = React.useState(new CountryInterest());
    const [title, setTitle] = React.useState("");
    const [open, setOpen] = React.useState(false);

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(event.target.value);
        setPage(0);
    };

    const handleClose = (event, reason) => {
        if (reason !== 'backdropClick') {
            setOpen(false);
        }
    };

    function showInterest(row) {
        new RestProvider().getCountryInterest(row.id)
            .then((countryInterest) => {
                setCountryInterest(countryInterest);
                setTitle("Interés del País");
                setOpen(true);
            })
    }

    return (
        <div>
            <InteresPaisDialog
                open={open}
                handleClose={handleClose}
                title={title}
                countryInterest={countryInterest}
            />
            <Typography className={classes.title} variant="h6" id="tableTitle" component="div">
                Ver Interés en Países
            </Typography>
            <Paper className={classes.root} style={{marginTop: "30px"}}>
                <TableContainer className={classes.container}>
                    <Table stickyHeader aria-label="sticky table" size="small">
                        <TableHead>
                            <TableRow>
                                {columns.map((column) => (
                                    <TableCell
                                        key={column.id}
                                        align={column.align}
                                        style={{minWidth: column.minWidth}}
                                    >
                                        {column.label}
                                    </TableCell>
                                ))}
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {
                                rows.length === 0 && isLoading && !error &&
                                <TableRow hover role="checkbox" tabIndex={-1}>
                                    <TableCell colSpan={3} align="center" style={{padding: "25px"}}>
                                        <CircularProgress size={60}/>
                                    </TableCell>
                                </TableRow>
                            }
                            {
                                rows.length === 0 && !isLoading && !error &&
                                <TableRow hover role="checkbox" tabIndex={-1}>
                                    <TableCell colSpan={3} style={{padding: "16px", fontWeight: "500"}}>
                                        No se encontraron registros...
                                    </TableCell>
                                </TableRow>
                            }
                            {
                                rows.length === 0 && error &&
                                <TableRow hover role="checkbox" tabIndex={-1}>
                                    <TableCell colSpan={3} style={{padding: "16px", fontWeight: "500"}}>
                                        Oops! Parece que hubo un error al intentar cargar, intente nuevamente más tarde.
                                    </TableCell>
                                </TableRow>
                            }
                            {rows.length > 0 && !error && rows.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((row) => {
                                return (
                                    <TableRow hover role="checkbox" tabIndex={-1} key={row.id}>
                                        <TableCell>
                                            {row.name}
                                        </TableCell>
                                        <TableCell>
                                            {row.iso_country_code}
                                        </TableCell>
                                        <TableCell align="center">
                                            <Tooltip title="Ver Interés" aria-label="add">
                                                <IconButton aria-label="interest" className={classes.margin}
                                                            size="small"
                                                            onClick={() => showInterest(row)}>
                                                    <VisibilityIcon/>
                                                </IconButton>
                                            </Tooltip>
                                        </TableCell>
                                    </TableRow>
                                );
                            })}
                        </TableBody>
                    </Table>
                </TableContainer>
                <TablePagination
                    rowsPerPageOptions={[10, 25, 100]}
                    component="div"
                    count={rows.length}
                    rowsPerPage={rowsPerPage}
                    page={page}
                    onChangePage={handleChangePage}
                    onChangeRowsPerPage={handleChangeRowsPerPage}
                />
            </Paper>
        </div>
    );
}