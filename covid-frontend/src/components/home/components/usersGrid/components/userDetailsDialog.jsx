import React from 'react';
import { withStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import MuiDialogTitle from '@material-ui/core/DialogTitle';
import MuiDialogContent from '@material-ui/core/DialogContent';
import MuiDialogActions from '@material-ui/core/DialogActions';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';
import Typography from '@material-ui/core/Typography';
import TextField from '@material-ui/core/TextField';

const styles = (theme) => ({
    root: {
        margin: 0,
        padding: theme.spacing(2),
    },
    closeButton: {
        position: 'absolute',
        right: theme.spacing(1),
        top: theme.spacing(1),
        color: theme.palette.grey[500],
    },
});

const DialogTitle = withStyles(styles)((props) => {
    const { children, classes, onClose, ...other } = props;
    return (
        <MuiDialogTitle disableTypography className={classes.root} {...other}>
            <Typography variant="h6">{children}</Typography>
            {onClose ? (
                <IconButton aria-label="close" className={classes.closeButton} onClick={onClose}>
                    <CloseIcon />
                </IconButton>
            ) : null}
        </MuiDialogTitle>
    );
});

const DialogContent = withStyles((theme) => ({
    root: {
        padding: theme.spacing(2),
    },
}))(MuiDialogContent);

const DialogActions = withStyles((theme) => ({
    root: {
        margin: 0,
        padding: theme.spacing(1),
    },
}))(MuiDialogActions);

export default function UserDetailsDialog(props) {

    return (
        <Dialog modal="true" onClose={props.handleClose} aria-labelledby="customized-dialog-title" open={props.open}>
            <DialogTitle id="customized-dialog-title" onClose={props.handleClose}>
                {props.title}
            </DialogTitle>
            <DialogContent dividers style={{ minWidth: "500px", display: "flex", flexDirection: "column" }} >
                <div>
                    <TextField
                        label="ID"
                        value={props.userInformation.id}
                        fullWidth
                    />
                    <TextField
                        style={{ marginTop: "16px" }}
                        label="Usuario"
                        value={props.userInformation.user_name}
                        fullWidth
                    />
                    <TextField
                        style={{ marginTop: "16px" }}
                        label="Nombre"
                        value={props.userInformation.name}
                        fullWidth
                    />
                    <TextField
                        style={{ marginTop: "16px" }}
                        label="Apellido"
                        value={props.userInformation.last_name}
                        fullWidth
                    />
                    <TextField
                        style={{ marginTop: "16px" }}
                        label="Usuario"
                        value={props.userInformation.email}
                        fullWidth
                    />
                    <TextField
                        style={{ marginTop: "16px" }}
                        label="Último Acceso"
                        value={props.userInformation.last_login}
                        fullWidth
                    />
                    <TextField
                        style={{ marginTop: "16px" }}
                        label="Telegram ID"
                        value={props.userInformation.telegram_id}
                        fullWidth
                    />
                    <TextField
                        style={{ marginTop: "16px" }}
                        label="Cantidad de Listas"
                        value={props.userInformation.total_lists}
                        fullWidth
                    />
                    <TextField
                        style={{ marginTop: "16px" }}
                        label="Cantidad de Países en sus Listas"
                        value={props.userInformation.total_countries}
                        fullWidth
                    />
                </div>
            </DialogContent>
            <DialogActions>
                <Button autoFocus onClick={props.handleClose} color="primary">
                    Salir
                </Button>
            </DialogActions>
        </Dialog>
    );
}