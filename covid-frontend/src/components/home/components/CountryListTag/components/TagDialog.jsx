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
import Select from '@material-ui/core/Select';
import InputLabel from '@material-ui/core/InputLabel';
import Input from '@material-ui/core/Input';
import MenuItem from '@material-ui/core/MenuItem';
import {Strategy} from '../../../../../models/Strategy'

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
    container: {
        minWidth: 150,
    },
});

//const strategies = ["No Analizado","Distanciamiento Social","Cuarentena","Libre Circulación"]
const strategies = Strategy;

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

export default function TagDialog(props) {
    return (
        <Dialog modal="true" onClose={props.handleClose} aria-labelledby="customized-dialog-title" open={props.open}>
            <DialogTitle id="customized-dialog-title" onClose={props.handleClose}>
                {props.title}
            </DialogTitle>
            <DialogContent dividers style={{ minWidth: "500px", display: "flex", flexDirection: "column" }} >
                <div>
                    <TextField
                        label="Nombre País"
                        value={props.countryTag.name}
                        fullWidth
                        disabled={true}
                    />
                     
                </div>
                <div>
                        <InputLabel style={{ marginTop: "30px" }} id="demo-dialog-select-label">Estrategia</InputLabel>
                        <Select
                            style={{ marginTop: "16px" }}
                            labelId="demo-dialog-select-label"
                            id="demo-dialog-select"
                            value={props.countryTag.strategy}
                            onChange={props.changeStrategy}
                            input={<Input/>}
                        >
                        {strategies.map((strategyCategory) => (
                            <MenuItem  value = {strategyCategory} >
                                {strategyCategory}
                            </MenuItem> ))}
                        </Select>                        
                </div>
            </DialogContent>
            <DialogActions>
            <Button autoFocus onClick={props.handleClose} color="primary">
                    Cancelar
                </Button>
                <Button autoFocus onClick={props.save} disabled={props.disabled} color="primary">
                    Guardar
                </Button>
            </DialogActions>
        </Dialog>
    );
}