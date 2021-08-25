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
import Button from "@material-ui/core/Button";
import CountryList from "../../../../models/CountryList";
import Tooltip from "@material-ui/core/Tooltip";
import IconButton from "@material-ui/core/IconButton";
import CreateIcon from "@material-ui/icons/Create";
import DeleteIcon from "@material-ui/icons/Delete";
import SaveListDialog from './components/saveListDialog';
import {toast} from "react-toastify";

const columns = [
    {id: 'name', label: 'Nombre', minWidth: 100},
    {id: 'iso_country_code', label: 'Código ISO', minWidth: 100},
    {
        id: 'actions',
        label: 'Agregar/Quitar de Lista Nueva',
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

export default function CountriesGrid(props) {
    const classes = useStyles();
    const [page, setPage] = React.useState(0);
    const [rowsPerPage, setRowsPerPage] = React.useState(10);
    const options = useMemo(() => ({}), [])
    const [rows, isLoading, error, refreshRows] = useGrid(getCountries, options);
    const [selectedList, setSelectedList] = React.useState(new CountryList());
    const [title, setTitle] = React.useState("");
    const [open, setOpen] = React.useState(false);

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(event.target.value);
        setPage(0);
    };

    function addCountry(row) {
        var selectedListAux = new CountryList();
        Object.assign(selectedListAux, selectedList)
        if (!selectedListAux.countries.some(item => item.name === row.name)) {
            selectedListAux.countries.push(row);
        }
        setSelectedList(selectedListAux);
    }

    function removeCountry(row) {
        var selectedListAux = new CountryList();
        Object.assign(selectedListAux, selectedList)
        if (selectedListAux.countries.some(item => item.name === row.name)) {
            selectedListAux.countries = selectedListAux.countries.filter(country => country.name !== row.name);
        }
        setSelectedList(selectedListAux);
    }

    const saveList = () => {
        if (selectedList.name.length > 0){
            new RestProvider().saveCountryList(selectedList)
                .then(() => {
                    refreshRows();
                    setOpen(false)
                })
            var selectedListAux = new CountryList();
            setSelectedList(selectedListAux);
        }else
        {
            toast.error("Elija un nombre para la lista")
        }
    }

    const handleClose = (event, reason) => {
        if (reason !== 'backdropClick') {
            setOpen(false);
        }
    };

    function confirmCreation() {
        if (selectedList.countries.length > 0) {
            setTitle("Guardar Lista");
            setOpen(true);
        } else {
            toast.error("Elija algún país para crear la lista");
        }
    }

    const changeSelectedName = (event) => {
        var selectedListAux = new CountryList();
        Object.assign(selectedListAux, selectedList)
        selectedListAux.name = event.target.value
        setSelectedList(selectedListAux);
    }

    function resetLista() {
        var selectedListAux = new CountryList();
        Object.assign(selectedList, selectedListAux)
        refreshRows();
    }

    return (
        <div>
            <SaveListDialog
                open={open}
                handleClose={handleClose}
                save={saveList}
                title={title}
                list={selectedList}
                changeName={changeSelectedName}
            />
            <Typography className={classes.title} variant="h6" id="tableTitle" component="div">
                Crear Lista
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
                                            <Tooltip title="Agregar" aria-label="add">
                                                <IconButton aria-label="edit" className={classes.margin} size="small"
                                                            onClick={() => addCountry(row)}>
                                                    <CreateIcon/>
                                                </IconButton>
                                            </Tooltip>
                                            <Tooltip title="Quitar" aria-label="remove">
                                                <IconButton aria-label="delete" className={classes.margin} size="small"
                                                            onClick={() => removeCountry(row)}>
                                                    <DeleteIcon/>
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

                <React.Fragment>
                    <ul className="list-group">
                        {selectedList.countries.map(listitem => (<Button
                            variant="contained"
                            color="primary">
                            {listitem.name}
                        </Button>))}
                    </ul>
                </React.Fragment>

                <Button variant="contained" color="primary" style={{float: 'right'}} onClick={() => confirmCreation()}>
                    Guardar Lista
                </Button>

                <Button variant="contained" color="primary" style={{float: 'right'}} onClick={() => resetLista()}>
                    Resetear Lista
                </Button>

            </Paper>
        </div>
    );
}