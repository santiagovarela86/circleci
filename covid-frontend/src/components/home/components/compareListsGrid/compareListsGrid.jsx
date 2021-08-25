import React, { useMemo } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TablePagination from '@material-ui/core/TablePagination';
import TableRow from '@material-ui/core/TableRow';
import Typography from '@material-ui/core/Typography';
import VisibilityIcon from '@material-ui/icons/Visibility'
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import useGrid from '../../../../hooks/useGrid';
import RestProvider from '../../../../rest/RestProvider'
import CircularProgress from '@material-ui/core/CircularProgress';
import { toast } from "react-toastify";
import Button from "@material-ui/core/Button";
import CompareListsDialog from "./components/compareListsDialog";
import Checkbox from '@material-ui/core/Checkbox';
import CompareIcon from '@material-ui/icons/Compare'

const columns = [
    { id: 'id', label: 'ID Lista', minWidth: 100 },
    { id: 'name', label: 'Nombre Lista', minWidth: 100 },
    { id: 'countries', label: 'Paises', minWidth: 100 },
    { id: 'user_id', label: 'ID User', minWidth: 100 },
    { id: 'creation_date', label: 'Fecha de Creación', minWidth: 100 },
];

const useStyles = makeStyles({
    root: {
        width: '100%',
    },
    container: {
        maxHeight: 500,
    },
});

const getAllCountryLists = new RestProvider().getAllCountryLists

export default function CompareListsGrid() {
    const classes = useStyles();
    const [page, setPage] = React.useState(0);
    const [rowsPerPage, setRowsPerPage] = React.useState(10);
    const [open, setOpen] = React.useState(false);
    const [title, setTitle] = React.useState("");
    const options = useMemo(() => ({}), [])
    const [rows, isLoading, error, refreshRows] = useGrid(getAllCountryLists, options);
    const [commonCountries, setCommonCountries] = React.useState([]);
    const [selected, setSelected] = React.useState([]);

    const handleClose = (event, reason) => {
        if (reason !== 'backdropClick') {
            setOpen(false);
        }
    };

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(+event.target.value);
        setPage(0);
    };

    const createTooltipPaises = (row) => {
        return (
            <div style={{ display: "flex", flexDirection: "column" }}>
                {row.countries.map((country) => {
                    return (
                        <div key={country.id}>
                            {country.iso_country_code + " - " + country.name}
                        </div>
                    )
                })}
            </div>
        )
    }

    const isSelected = (name) => selected.indexOf(name) !== -1;

    const handleClick = (event, name) => {
        const selectedIndex = selected.indexOf(name);
        let newSelected = [];

        if (selectedIndex === -1 && selected.length < 2) {
            newSelected = newSelected.concat(selected, name);
        } else if (selectedIndex === -1 && selected.length >= 2) {
            newSelected = selected;
        } else if (selectedIndex === 0) {
            newSelected = newSelected.concat(selected.slice(1));
        } else if (selectedIndex === selected.length - 1) {
            newSelected = newSelected.concat(selected.slice(0, -1));
        } else if (selectedIndex > 0) {
            newSelected = newSelected.concat(
                selected.slice(0, selectedIndex),
                selected.slice(selectedIndex + 1),
            );
        }

        setSelected(newSelected);
    };

    function compareLists() {
        if (selected.length !== 2) {
            toast.error("Debe elegir dos listas para comparar.");
        } else {
            new RestProvider().listsCompare(selected[0], selected[1])
            .then((commonCountries) => {
                setCommonCountries(commonCountries);
                if (commonCountries.length > 0) {
                    setTitle("Listado de Países en Común");
                    setOpen(true);
                } else {
                    toast.info("Esas listas no tienen países en común.");
                }
            });
            refreshRows();
        }
    }

    return (
        <div>
            <CompareListsDialog
                open={open}
                handleClose={handleClose}
                title={title}
                commonCountries={commonCountries}
            />
            <Typography className={classes.title} variant="h6" id="tableTitle" component="div">
                Comparar Listas
            </Typography>
            <Button startIcon={<CompareIcon />} variant="contained" color="primary" style={{ marginTop: "10px" }} onClick={() => compareLists()}>
                Comparar Listas
            </Button>
            <Paper className={classes.root} style={{ marginTop: "20px" }} >
                <TableContainer className={classes.container}>
                    <Table stickyHeader aria-label="sticky table" size="small">
                        <TableHead>
                            <TableRow>
                                <TableCell>
                                </TableCell>
                                {columns.map((column) => (
                                    <TableCell
                                        key={column.id}
                                        align={column.align}
                                        style={{ minWidth: column.minWidth }}
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
                                    <TableCell colSpan={6} align="center" style={{ padding: "25px" }}>
                                        <CircularProgress size={60} />
                                    </TableCell>
                                </TableRow>
                            }
                            {
                                rows.length === 0 && !isLoading && !error &&
                                <TableRow hover role="checkbox" tabIndex={-1}>
                                    <TableCell colSpan={6} style={{ padding: "16px", fontWeight: "500" }}>
                                        No se encontraron registros...
                                    </TableCell>
                                </TableRow>
                            }
                            {
                                rows.length === 0 && error &&
                                <TableRow hover role="checkbox" tabIndex={-1}>
                                    <TableCell colSpan={6} style={{ padding: "16px", fontWeight: "500" }}>
                                        Oops! Parece que hubo un error al intentar cargar, intente nuevamente más tarde.
                                    </TableCell>
                                </TableRow>
                            }
                            {rows.length > 0 && !error && rows.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((row) => {
                                const isItemSelected = isSelected(row.id);
                                return (
                                    <TableRow onClick={(event) => handleClick(event, row.id)} hover role="checkbox" selected={isItemSelected} tabIndex={-1} key={row.id}>
                                        <TableCell>
                                            <Checkbox disabled={selected.length >= 2 && !isItemSelected} checked={isItemSelected} color="secondary" />
                                        </TableCell>
                                        <TableCell>
                                            {row.id}
                                        </TableCell>
                                        <TableCell>
                                            {row.name}
                                        </TableCell>
                                        <TableCell>
                                            <Tooltip title={createTooltipPaises(row)} arrow placement="right">
                                                <IconButton aria-label="paises" className={classes.margin} size="small">
                                                    <VisibilityIcon />
                                                </IconButton>
                                            </Tooltip>
                                        </TableCell>
                                        <TableCell>
                                            {row.user_id}
                                        </TableCell>
                                        <TableCell>
                                            {row.creation_date}
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