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
import CreateIcon from '@material-ui/icons/Create'
import DeleteIcon from '@material-ui/icons/Delete'
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import ListDialog from './components/listDialog';
import CountryList from '../../../../models/CountryList';
import useCountries from '../../../../hooks/useCountries';
import useGrid from '../../../../hooks/useGrid';
import RestProvider from '../../../../rest/RestProvider'
import CircularProgress from '@material-ui/core/CircularProgress';
import ChartIcon from '@material-ui/icons/InsertChartOutlined'
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import Button from "@material-ui/core/Button";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContentText from "@material-ui/core/DialogContentText";
import { toast } from 'react-toastify';
import AddIcon from '@material-ui/icons/Add'

const columns = [
  { id: 'name', label: 'Nombre', minWidth: 100 },
  { id: 'countries', label: 'Paises', minWidth: 100 },
  { id: 'creation_date', label: 'Fecha de Creación', minWidth: 100 },
  {
    id: 'actions',
    label: 'Acciones',
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

const getCountryLists = new RestProvider().getCountryLists;

export default function ListsGrid(props) {
  const classes = useStyles();
  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(10);
  const [open, setOpen] = React.useState(false);
  const [openConfirmation, setOpenConfirmation] = React.useState(false);
  const [title, setTitle] = React.useState("");
  const [selectedList, setSelectedList] = React.useState(new CountryList());
  const [disableDeleteButton, setDisableDeleteButton] = React.useState(false);
  const countries = useCountries();
  const options = useMemo(() => ({}), [])
  const [rows, isLoading, error, refreshRows] = useGrid(getCountryLists, options);
  const [isLoadingDialog, setIsLoadingDialog] = React.useState(false);

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

  const createTooltip = (row) => {
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

  const changeSelectedName = (event) => {
    var selectedListAux = new CountryList();
    Object.assign(selectedListAux, selectedList)
    selectedListAux.name = event.target.value
    setSelectedList(selectedListAux);
  }

  const changeSelectedCountries = (event) => {
    var selectedListAux = new CountryList();
    Object.assign(selectedListAux, selectedList);
    selectedListAux.countries = countries.filter(country => event.target.value.some(value => value === country.id));
    setSelectedList(selectedListAux);
  }

  const modify = (list) => {
    setSelectedList(list);
    setTitle("Modificar Lista");
    setOpen(true);
    setIsLoadingDialog(false);
  }

  const create = () => {
    setSelectedList(new CountryList());
    setTitle("Crear Lista");
    setOpen(true);
    setIsLoadingDialog(false);
  }

  const handleSave = () => {
    if (selectedList.name.length > 0 && selectedList.countries.length > 0) {
      setIsLoadingDialog(true);
      if (selectedList.id === 0) {
        new RestProvider().createCountryList(selectedList)
          .then(() => {
            setOpen(false);
            refreshRows();
          }).catch(() => {
            setIsLoadingDialog(false);
          })
      } else {
        new RestProvider().updateCountryList(selectedList)
          .then(() => {
            setOpen(false);
            refreshRows();
          }).catch(() => {
            setIsLoadingDialog(false);
          })
      }
    } else {
      if (selectedList.name.length <= 0) {
        toast.error("Elija un nombre para la lista");
      } else {
        toast.error("La lista no puede quedar sin países");
      }
    }
  }

  const handleClickDelete = (list) => {
    setSelectedList(list);
    setOpenConfirmation(true);
  };

  const handleCloseConfirmation = () => {
    setOpenConfirmation(false);
  };

  const deleteCountryList = () => {
    setDisableDeleteButton(true);
    new RestProvider().deleteCountryList(selectedList)
      .then(() => {
        setOpenConfirmation(false)
        toast.success("Se ha eliminado la lista correctamente");
        refreshRows();
      })
      .catch(err => toast.error("Error al eliminar la lista"))
      .finally(() => {
        setDisableDeleteButton(false);
      })
  }

  return (
    <div>
      <Dialog modal="true" onClose={handleCloseConfirmation} aria-labelledby="customized-dialog-title" open={openConfirmation}>
        <DialogTitle id="alert-dialog-title">{"¿Está seguro que desea eliminar la lista?"}</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            Si no desea eliminar la lista, haga clic en cancelar.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseConfirmation} color="primary">
            Cancelar
          </Button>
          <Button onClick={deleteCountryList} color="primary" disabled={disableDeleteButton}>
            Eliminar Lista
          </Button>
        </DialogActions>
      </Dialog>
      <ListDialog
        open={open}
        handleClose={handleClose}
        handleSave={handleSave}
        title={title}
        list={selectedList}
        changeName={changeSelectedName}
        changeCountries={changeSelectedCountries}
        countries={countries}
        save={handleSave}
        disabled={isLoadingDialog}
      />
      <Typography className={classes.title} variant="h6" id="tableTitle" component="div">
        Mis Listas
      </Typography>
      <Button
        style={{ marginTop: "10px" }}
        variant="contained"
        color="primary"
        onClick={create}
        startIcon={<AddIcon />}
      >
        Crear
      </Button>
      <Paper className={classes.root} style={{ marginTop: "20px" }} >
        <TableContainer className={classes.container}>
          <Table stickyHeader aria-label="sticky table" size="small">
            <TableHead>
              <TableRow>
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
                  <TableCell colSpan={4} align="center" style={{ padding: "25px" }}>
                    <CircularProgress size={60} />
                  </TableCell>
                </TableRow>
              }
              {
                rows.length === 0 && !isLoading && !error &&
                <TableRow hover role="checkbox" tabIndex={-1}>
                  <TableCell colSpan={4} style={{ padding: "16px", fontWeight: "500" }}>
                    No se encontraron registros...
                </TableCell>
                </TableRow>
              }
              {
                rows.length === 0 && error &&
                <TableRow hover role="checkbox" tabIndex={-1}>
                  <TableCell colSpan={4} style={{ padding: "16px", fontWeight: "500" }}>
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
                      <Tooltip title={createTooltip(row)} arrow placement="right">
                        <IconButton aria-label="delete" className={classes.margin} size="small">
                          <VisibilityIcon />
                        </IconButton>
                      </Tooltip>
                    </TableCell>
                    <TableCell>
                      {row.creation_date}
                    </TableCell>
                    <TableCell align="center">
                      <Tooltip title="Editar" aria-label="edit">
                        <IconButton aria-label="edit" className={classes.margin} size="small" onClick={() => modify(row)}>
                          <CreateIcon />
                        </IconButton>
                      </Tooltip>
                      <Tooltip title="Eliminar" aria-label="delete">
                        <IconButton aria-label="delete" className={classes.margin} size="small" onClick={() => handleClickDelete(row)}>
                          <DeleteIcon />
                        </IconButton>
                      </Tooltip>
                      <Tooltip title="Generar gráfico" aria-label="chart">
                        <IconButton aria-label="chart" className={classes.margin} size="small" onClick={() => props.goToPlot(row)}>
                          <ChartIcon />
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