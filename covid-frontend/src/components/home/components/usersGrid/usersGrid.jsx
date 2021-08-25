import React, {useMemo} from 'react';
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
import RestProvider from "../../../../rest/RestProvider";
import useGrid from "../../../../hooks/useGrid";
import UserDetailsDialog from "./components/userDetailsDialog";
import UserInformation from "../../../../models/UserInformation";
import CircularProgress from "@material-ui/core/CircularProgress";
import Tooltip from "@material-ui/core/Tooltip";

const columns = [
  { id: 'id', label: 'ID', minWidth: 100 },
  { id: 'user_name', label: 'Usuario', minWidth: 100 },
  { id: 'name', label: 'Nombre', minWidth: 100 },
  { id: 'last_name', label: 'Apellido', minWidth: 100 },
  { id: 'email', label: 'Correo', minWidth: 100 },
  { id: 'last_login', label: 'Último Acceso', minWidth: 100 },
  {
    id: 'actions',
    label: 'Ver detalles',
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

const getUsers = new RestProvider().getAllUsers

export default function UsersGrid(props) {
  const classes = useStyles();
  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(10);
  const options = useMemo(() => ({}), [])
  const [rows, isLoading, error] = useGrid(getUsers, options);
  const [title, setTitle] = React.useState("");
  const [open, setOpen] = React.useState(false);
  const [selectedUserInformation, setSelectedUserInformation] = React.useState(new UserInformation());

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

  function viewDetails(row) {
    var userInformationPromise = new RestProvider().getUserInformation(row.id);
    userInformationPromise.then((userInformation) => {
      setSelectedUserInformation(userInformation);
      setTitle("Detalles Usuario");
      setOpen(true);
    });
  }

  return (
    <div>
      <UserDetailsDialog
          open={open}
          handleClose={handleClose}
          title={title}
          userInformation={selectedUserInformation}
      />
      <Typography className={classes.title} variant="h6" id="tableTitle" component="div">
        Usuarios
      </Typography>
      <Paper className={classes.root} style={{ marginTop: "30px" }} >
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
                  <TableCell colSpan={7} align="center" style={{ padding: "25px" }}>
                    <CircularProgress size={60} />
                  </TableCell>
                </TableRow>
              }
              {
                rows.length === 0 && !isLoading && !error &&
                <TableRow hover role="checkbox" tabIndex={-1}>
                  <TableCell colSpan={7} style={{ padding: "16px", fontWeight: "500" }}>
                    No se encontraron registros...
                  </TableCell>
                </TableRow>
              }
              {
                rows.length === 0 && error &&
                <TableRow hover role="checkbox" tabIndex={-1}>
                  <TableCell colSpan={7} style={{ padding: "16px", fontWeight: "500" }}>
                    Oops! Parece que hubo un error al intentar cargar, intente nuevamente más tarde.
                  </TableCell>
                </TableRow>
              }
              {rows.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((row) => {
                return (
                  <TableRow hover role="checkbox" tabIndex={-1} key={row.id}>
                    <TableCell>
                      {row.id}
                    </TableCell>
                    <TableCell>
                      {row.user_name}
                    </TableCell>
                    <TableCell>
                      {row.name}
                    </TableCell>
                    <TableCell>
                      {row.last_name}
                    </TableCell>
                    <TableCell>
                      {row.email}
                    </TableCell>
                    <TableCell>
                      {row.last_login}
                    </TableCell>
                    <TableCell align="center">
                      <Tooltip title="Ver detalles" aria-label="add">
                        <IconButton aria-label="details" className={classes.margin} size="small"
                                    onClick={() => viewDetails(row)}>
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
          count={props.rows.length}
          rowsPerPage={rowsPerPage}
          page={page}
          onChangePage={handleChangePage}
          onChangeRowsPerPage={handleChangeRowsPerPage}
        />
      </Paper>
    </div>
  );
}