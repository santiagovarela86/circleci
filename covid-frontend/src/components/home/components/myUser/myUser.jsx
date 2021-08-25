import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import TextField from '@material-ui/core/TextField';

const useStyles = makeStyles({
  root: {
    width: '100%',
  },
  container: {
    maxHeight: 500,
  },
});

export default function MyUse(props) {
  const classes = useStyles();

  return (
    <div>
      <Typography style={{ marginBottom: "10px" }} className={classes.title} variant="h6" id="tableTitle" component="div">
        Mi Perfil
      </Typography>
      <div style={{ display: "flex", flexDirection: "column" }}>
        <TextField
          label="Usuario"
          value={props.userLogged.user_name}
          disabled
        />
        <TextField
          style={{ marginTop: "20px" }}
          label="Nombre"
          value={props.userLogged.name}
          disabled
        />
        <TextField
          style={{ marginTop: "20px" }}
          label="Apellido"
          value={props.userLogged.last_name}
          disabled
        />
        <TextField
          style={{ marginTop: "20px" }}
          label="Email"
          value={props.userLogged.email}
          disabled
        />
      </div>
    </div>
  );
}