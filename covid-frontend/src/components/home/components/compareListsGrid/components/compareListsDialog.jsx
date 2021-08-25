import Dialog from "@material-ui/core/Dialog";
import Button from "@material-ui/core/Button";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import Paper from "@material-ui/core/Paper";
import TableContainer from "@material-ui/core/TableContainer";
import TableRow from "@material-ui/core/TableRow";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import Table from "@material-ui/core/Table";
import React from "react";

export default function CompareListsDialog(props) {
    return (
        <Dialog modal="true" onClose={props.handleClose} aria-labelledby="customized-dialog-title" open={props.open}>
            <DialogTitle id="customized-dialog-title" onClose={props.handleClose}>
                {props.title}
            </DialogTitle>
            <DialogContent dividers style={{ minWidth: "500px", display: "flex", flexDirection: "column" }} >
                <TableContainer component={Paper}>
                    <Table aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell>Nombre</TableCell>
                                <TableCell>Código ISO</TableCell>
                                <TableCell>Fecha Primer Caso</TableCell>
                                <TableCell>Offset</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {props.commonCountries.map((row) => (
                                <TableRow key={row.id}>
                                    <TableCell>
                                        {row.name}
                                    </TableCell>
                                    <TableCell>
                                        {row.iso_country_code}
                                    </TableCell>
                                    <TableCell >
                                        {row.start_date}
                                    </TableCell>
                                    <TableCell >
                                        {row.offset}
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </DialogContent>
            <DialogActions>
                <Button autoFocus onClick={props.handleClose} color="primary">
                    Salir
                </Button>
            </DialogActions>
        </Dialog>
    );
}