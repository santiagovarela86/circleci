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
import TagDialog from "./components/TagDialog";
import Country from "../../../../models/Country";
import CreateIcon from '@material-ui/icons/Create'
import { toast } from 'react-toastify';

const columns = [
    {id: 'name', label: 'Nombre', minWidth: 100},
    {id: 'estratgia', label: 'Estrategia', minWidth: 100},
    {id: 'acciones', label: 'Acciones', minWidth: 100},
    
];

const useStyles = makeStyles({
    root: {
        width: '100%',
    },
    container: {
        minWidth: 500,
    },
});


const getCountries = new RestProvider().getCountries

export default function CountryListTag(props) {
    const classes = useStyles();
    const [page, setPage] = React.useState(0);
    const [rowsPerPage, setRowsPerPage] = React.useState(10);
    const options = useMemo(() => ({}), [])
    const [rows, isLoading, error,refreshRows] = useGrid(getCountries, options);
    const [countryTag, setCountryTag] = React.useState(new Country());
    const [title, setTitle] = React.useState("");
    const [strategy, setStrategy] = React.useState("");
    const [open, setOpen] = React.useState(false);
    const [isLoadingDialog, setIsLoadingDialog] = React.useState(false);

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

    const handleSave = () => {
        //setramos la nueva estrategia 
        if(strategy){
            //setIsLoadingDialog(true);
            countryTag.strategy = strategy;
            new RestProvider().updateCountry(countryTag)
                .then((countryUpdate) => {
                    console.log(countryUpdate);
                    setOpen(false);
                    refreshRows();
                }).catch(() => {
                    setIsLoadingDialog(false);
                });
            
        }else{
            toast.error("Elija una estrategia");
        }
      }

    const changeSelectedStrategy = (event) => {
    event.preventDefault();
            if(event.target.value){
                setStrategy(event.target.value);
                countryTag.strategy = event.target.value ;
                console.log(countryTag);
                setIsLoadingDialog(false);
            }else{
                toast.error("Elija una estrategia");            }
            }   

    function showTag(row) {
        new RestProvider().getCountryId(row.id)
            .then((countryResponse) => {
                setIsLoadingDialog(true);
                setCountryTag(countryResponse);
                setTitle("Editar Categoria");
                setOpen(true);
            })
    }

    return (
        <div>
            <TagDialog
                open={open}
                handleClose={handleClose}
                handleSave={handleSave}
                save={handleSave}
                changeStrategy={changeSelectedStrategy}
                title={title}
                countryTag={countryTag}
                disabled={isLoadingDialog}
            />
            <Typography className={classes.title} variant="h6" id="tableTitle" component="div">
                Paises
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
                                    <TableCell colSpan={3} align="right" style={{padding: "25px"}}>
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
                                        <TableCell >
                                            {row.name}
                                        </TableCell>
                                        <TableCell >
                                            {row.strategy}
                                        </TableCell>
                                        <TableCell >
                                            <Tooltip title="Editar Categoria" aria-label="add">
                                                <IconButton aria-label="edit" className={classes.margin}
                                                            size="small"
                                                            onClick={() => showTag(row)}>
                                                    <CreateIcon/>
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