/*
- Nombre: Rodrigo Emmanuel Díaz García
- Carné: 2022178
- Código Técnico: IN5AM
- Fecha de creación: 28/03/2023
- Fechas de modificación:
		28/03/2023:
			Creamos las entidades ya normalizadas para el proyecto
		29/03/2023: 
			Creamos los procedimientos almacenados para las entidades que no tienen llave foránea
		30/03/2023:
			Creamos los procedimientos almacenados para las entidades con llave foránea
		31/03/2023:
			Agregamos un dato a cada entidad.
		10/05/2023:
			Modificamos los procedimientos almacenados de editar todas las entidades para que no pueda actualizar las primary keys
			ni las foreing keys, y agregamos 10 registros a cada entidad
		23/05/2023:
			Creamos la consulta general
		31/05/2023:
			Creamos la entidad de Usuario y login y creamos sus procedimientos almacenados
*/

Drop database if exists DBTonysKinal2023;

Create database DBTonysKinal2023;

use DBTonysKinal2023;

Create table Empresas
(
	codigoEmpresa int auto_increment not null,
    nombreEmpresa varchar(150) not null,
    direccion varchar(150) not null,
    telefono varchar(8),
    primary key PK_codigoEmpresa (codigoEmpresa)
);

Create table TipoEmpleado
(
	codigoTipoEmpleado int not null auto_increment,
    descripcion varchar(50) not null,
    primary key PK_codigoTipoEmpleado (codigoTipoEmpleado)
);

Create table TipoPlato
(
	codigoTipoPlato int not null auto_increment,
    descripcionTipo varchar(100) not null,
    primary key PK_codigoTipoPlato (codigoTipoPlato)
);

Create table Productos
(
	codigoProducto int not null auto_increment,
    nombreProducto varchar(150) not null,
    cantidadProducto int not null,
    primary key PK_codigoProducto (codigoProducto)
    
);

Create table Empleados
(
	codigoEmpleado int not null auto_increment,
    numeroEmpleado int not null,
    apellidosEmpleado varchar(150) not null,
    nombresEmpleado varchar(150) not null,
    direccionEmpleado varchar(150) not null,
    telefonoContacto varchar(8) not null,
    gradoCocinero varchar(50),
    codigoTipoEmpleado int not null,
    primary key PK_codigoEmpleado (codigoEmpleado),
    constraint FK_Empleados_TipoEmpleado 
		foreign key(codigoTipoEmpleado)
        references TipoEmpleado(codigoTipoEmpleado)
);

Create table Servicios
(
	codigoServicio int not null auto_increment,
    fechaServicio date not null,
    tipoServicio varchar(150) not null,
    horaServicio time not null,
    lugarServicio varchar(150) not null,
    telefonoContacto varchar(50) not null,
    codigoEmpresa int not null,
    primary key PK_codigoServicio (codigoServicio),
    constraint FK_Servicios_Empresas foreign key(codigoEmpresa)
		references Empresas(codigoEmpresa)
);

Create table Presupuesto
(
	codigoPresupuesto int not null auto_increment,
    fechaSolicitud date not null,
    cantidadPresupuesto decimal(10,2) not null,
    codigoEmpresa int not null,
    primary key PK_codigoPresupuesto (codigoPresupuesto),
    constraint FK_Presupuesto_Empresas foreign key (codigoEmpresa)
		references Empresas(codigoEmpresa)
);

Create table Platos
(
	codigoPlato int not null auto_increment,
    cantidad int not null,
    nombrePlato varchar(150) not null,
    descripcionPlato varchar(150) not null,
    precioPlato decimal(10,2) not null,
    codigoTipoPlato int not null,
    primary key PK_codigoPlato (codigoPlato),
    constraint FK_Platos_TipoPlato foreign key (codigoTipoPlato)
		references TipoPlato(codigoTipoPlato)
);

Create table Productos_has_Platos
(
	Productos_codigoProducto int not null,
    codigoProducto int not null,
    codigoPlato int not null,
    primary key PK_Productos_codigoProducto (Productos_codigoProducto),
    constraint FK_Productos_has_Platos_Productos foreign key (codigoProducto)
		references Productos(codigoProducto),
	constraint FK_Producto_has_Platos_Platos foreign key (codigoPlato)
		references Platos(codigoPlato)
);

Create table Servicios_has_Platos
(
	Servicios_codigoServicios int not null,
    codigoPlato int not null,
    codigoServicio int not null,
    primary key PK_Servicios_codigoServicios (Servicios_codigoServicios),
    constraint FK_Servicios_has_Platos_Servicios foreign key (codigoServicio)
		references Servicios(codigoServicio),
	constraint FK_Servicios_has_Platos_Platos foreign key (codigoPlato)
		references Platos(codigoPlato)
);

Create table Servicios_has_Empleados
(
	Servicios_codigoServicio int not null,
    codigoServicio int not null,
    codigoEmpleado int not null,
    fechaEvento date not null,
    horaEvento time not null,
    lugarEvento varchar(150) not null,
    primary key PK_Servicios_codigoServicio (Servicios_codigoServicio),
    constraint FK_Servicios_has_Empleados_Servicios foreign key (codigoServicio)
		references Servicios(codigoServicio),
	constraint FK_Servicios_has_Empleados_Empleados foreign key (codigoEmpleado)
		references Empleados(codigoEmpleado)
);

Create table Usuario
(
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar(100) not null,
    apellidoUsuario varchar(50) not null,
    usuarioLogin varchar(50) not null unique,
    contrasena varchar(150) not null,
    primary key PK_codigoUsuario (codigoUsuario)
);

Create table Login
(
	usuarioMaster varchar(50) not null,
    passwordLogin varchar(50) not null,
    primary key PK_usuarioMaster (usuarioMaster)
);



-- --------- PROCEDIMIENTOS ALMACENADOS ----------

-- -------- EMPRESAS ---------------

select * from Empresas;

-- Agregar Empresa

Delimiter //
	Create procedure sp_AgregarEmpresa(
		in nombreEmpresa varchar(150),
		in direccion varchar(150),
        in telefono varchar(8))
		Begin
			insert into Empresas(nombreEmpresa, direccion, telefono)
				values(nombreEmpresa, direccion, telefono);
        End //
Delimiter ;

call sp_AgregarEmpresa('Hino', '11 av B Mixco', '45782406');
call sp_AgregarEmpresa('Shell', '19-25 zona 10 Guatemala', '45781236');
call sp_AgregarEmpresa('Dell', '45-78 av Incapíe', '14687035');
call sp_AgregarEmpresa('Kinal', 'Zona 21 colonia sol', '78914503');
call sp_AgregarEmpresa('Toyota', 'Colonia Mariscal 2', '87456930');
call sp_AgregarEmpresa('Canella', 'Avenida Periferico sur', '10324785');
call sp_AgregarEmpresa('Agua pura salva vidas', 'Avenida petapa zona 13', '24365897');
call sp_AgregarEmpresa('Samsung', 'Usac av Petapa', '48952016');
call sp_AgregarEmpresa('Eskala', 'Tikal 2 zona 7', '65987402');
call sp_AgregarEmpresa('Super 24', 'colonia perez Guissasola zona 10', '40126573');


-- Listar Empresas

Delimiter //
	create procedure sp_ListarEmpresas()
		Begin
			Select
				E.codigoEmpresa,
				E.nombreEmpresa,
                E.direccion,
                E.telefono
			from Empresas E;
        End //
Delimiter ;


-- Buscar Empresa

Delimiter //
	Create procedure sp_BuscarEmpresa(in id int)
		Begin
			Select
				E.codigoEmpresa,
				E.nombreEmpresa,
                E.direccion,
                E.telefono
			from Empresas E where E.codigoEmpresa = id;
        end //
Delimiter ;


-- Editar Empresa

Delimiter //
	create procedure sp_EditarEmpresa(
		in id int,
        in nomEm varchar(150),
		in dire varchar(150),
        in tel varchar(8))
        Begin
			update Empresas E set
				E.nombreEmpresa = nomEm,
                E.direccion = dire,
                E.telefono = tel
                where E.codigoEmpresa = id;
                
        End //
Delimiter ;


-- Eliminar Empresa

Delimiter //
	Create procedure sp_EliminarEmpresa(in id int)
		Begin
			Delete from Empresas where codigoEmpresa = id;
        End //
Delimiter ;


-- -------- TIPO EMPLEADO ---------------

select * from TipoEmpleado;

-- Agregar TipoEmpleado

Delimiter //
	Create procedure sp_AgregarTipoEmpleado(in descripcion varchar(150))
		Begin
			insert into TipoEmpleado(descripcion)
				values(descripcion);
        End //
Delimiter ;

call sp_AgregarTipoEmpleado('Consultor');
call sp_AgregarTipoEmpleado('Chef');
call sp_AgregarTipoEmpleado('Mesero');
call sp_AgregarTipoEmpleado('Repartidor');
call sp_AgregarTipoEmpleado('Gerente');
call sp_AgregarTipoEmpleado('Secretario');
call sp_AgregarTipoEmpleado('Piloto');
call sp_AgregarTipoEmpleado('Supervisor');
call sp_AgregarTipoEmpleado('Seguridad');
call sp_AgregarTipoEmpleado('Empacador');


-- Listar TipoEmpleado

Delimiter //
	create procedure sp_ListarTipoEmpleados()
		Begin
			Select
				T.codigoTipoEmpleado,
				T.descripcion
			from TipoEmpleado T;
        End //
Delimiter ;


-- Buscar TipoEmpleado

Delimiter //
	Create procedure sp_BuscarTipoEmpleado(in id int)
		Begin
			Select
				T.codigoTipoEmpleado,
				T.descripcion
			from TipoEmpleado T where T.codigoTipoEmpleado = id;
        end //
Delimiter ;


-- Editar TipoEmpleado

Delimiter //
	create procedure sp_EditarTipoEmpleado(in id int, in descri varchar(150))
        Begin
			update TipoEmpleado T set
				T.descripcion = descri
                where T.codigoTipoEmpleado = id;
                
        End //
Delimiter ;



-- Eliminar TipoEmpleado

Delimiter //
	Create procedure sp_EliminarTipoEmpleado(in id int)
		Begin
			Delete from TipoEmpleado where codigoTipoEmpleado = id;
        End //
Delimiter ;


-- -------- TIPO PLATO ---------------

select * from TipoPlato;

-- Agregar TipoPlato

Delimiter //
	Create procedure sp_AgregarTipoPlato(in descripcion varchar(150))
		Begin
			insert into TipoPlato(descripcionTipo)
				values(descripcion);
        End //
Delimiter ;

call sp_AgregarTipoPlato('Tropical');
call sp_AgregarTipoPlato('Japones');
call sp_AgregarTipoPlato('Sureño');
call sp_AgregarTipoPlato('Mariscos');
call sp_AgregarTipoPlato('Mar y tierra');
call sp_AgregarTipoPlato('Chapín');
call sp_AgregarTipoPlato('Español');
call sp_AgregarTipoPlato('Oriental');
call sp_AgregarTipoPlato('Entrada');
call sp_AgregarTipoPlato('Caliente');

-- Listar TipoPlato

Delimiter //
	create procedure sp_ListarTipoPlatos()
		Begin
			Select
				T.codigoTipoPlato,
				T.descripcionTipo
			from TipoPlato T;
        End //
Delimiter ;



-- Buscar TipoPlato

Delimiter //
	Create procedure sp_BuscarTipoPlato(in id int)
		Begin
			Select
				T.codigoTipoPlato,
				T.descripcionTipo
			from TipoPlato T where T.codigoTipoPlato = id;
        end //
Delimiter ;



-- Editar TipoPlato

Delimiter //
	create procedure sp_EditarTipoPlato(in id int, in descri varchar(150))
        Begin
			update TipoPlato T set
				T.descripcionTipo = descri
                where T.codigoTipoPlato = id;
        End //
Delimiter ;


-- Eliminar TipoPlato

Delimiter //
	Create procedure sp_EliminarTipoPlato(in id int)
		Begin
			Delete from TipoPlato where codigoTipoPlato = id;
        End //
Delimiter ;


-- -------- PRODUCTOS ---------------

select * from Productos;

-- Agregar Producto

Delimiter //
	Create procedure sp_AgregarProducto(in nombre varchar(150), in cantidad int)
		Begin
			insert into Productos(nombreProducto, cantidadProducto)
				values(nombre, cantidad);
        End //
Delimiter ;

call sp_AgregarProducto('Manzana', 5);
call sp_AgregarProducto('cebollas', 55);
call sp_AgregarProducto('Tomates', 48);
call sp_AgregarProducto('Ajos', 100);
call sp_AgregarProducto('Platos', 46);
call sp_AgregarProducto('Cucharas', 10);
call sp_AgregarProducto('manteles', 3);
call sp_AgregarProducto('silantro', 78);
call sp_AgregarProducto('Cervezas', 100);
call sp_AgregarProducto('Pepsi', 65);


-- Listar Producto

Delimiter //
	create procedure sp_ListarProductos()
		Begin
			Select
            
				P.codigoProducto,
				P.nombreProducto,
                P.cantidadProducto
			from Productos P;
        End //
Delimiter ;



-- Buscar Producto

Delimiter //
	Create procedure sp_BuscarProducto(in id int)
		Begin
			Select
				P.codigoProducto,
				P.nombreProducto,
                P.cantidadProducto
			from Productos P where P.codigoProducto = id;
        end //
Delimiter ;



-- Editar Producto

Delimiter //
	create procedure sp_EditarProducto(in id int, in nom varchar(150), in cant int)
        Begin
			update Productos P set
				P.nombreProducto = nom,
                P.cantidadProducto = cant
                where P.codigoProducto = id;
        End //
Delimiter ;



-- Eliminar Producto

Delimiter //
	Create procedure sp_EliminarProducto(in id int)
		Begin
			Delete from Productos where codigoProducto = id;
        End //
Delimiter ;


-- -------- EMPLEADOS ---------------

select * from Empleados;
describe Empleados;

-- Agregar Empleado

Delimiter //
	Create procedure sp_AgregarEmpleado(
		in numero int,
        in apellidos varchar(150),
        in nombres varchar(150),
        in direccion varchar(150),
        in telefono varchar(8),
        in grado varchar(50),
        in codigoTipo int)
		Begin
			insert into Empleados(
				numeroEmpleado,
                apellidosEmpleado,
                nombresEmpleado,
                direccionEmpleado,
                telefonoContacto,
                gradoCocinero,
                codigoTipoEmpleado)
					values(numero, apellidos, nombres, direccion, telefono, grado, codigoTipo);
        End //
Delimiter ;

call sp_AgregarEmpleado(1, 'Perez', 'Rodrigo', '11 av B', '57842106', 'primero', 1);
call sp_AgregarEmpleado(1, 'Gonzalez', 'Esteban', 'zona 15 landivar', '47851206', 'Segundo', 2);
call sp_AgregarEmpleado(1, 'Ordoñez', 'Joaquin', 'zon 6 sagraderos', '40123698', 'Tercero', 4);
call sp_AgregarEmpleado(1, 'Armas', 'Samuel', 'colonia landivar 4-5', '47850324', 'primero', 6);
call sp_AgregarEmpleado(1, 'Escobar', 'Llanel', 'colonia minerva 78-2', '02587469', 'tercero', 10);
call sp_AgregarEmpleado(1, 'Cruz', 'Santiago', 'colonia satelite 45-02', '75315964', 'segundo', 1);
call sp_AgregarEmpleado(1, 'Vega', 'Javier', '40-36 zona 2 de mixco', '63571298', 'primero', 7);
call sp_AgregarEmpleado(1, 'Suarez', 'Luis', '45-85 zona 8', '03214567', 'tercero', 3);
call sp_AgregarEmpleado(1, 'Díaz', 'Emmanuel', '12 av C 19-57', '74185260', 'primero', 8);
call sp_AgregarEmpleado(1, 'García', 'Brayan', 'zona 10 eskala', '45678912', 'primero', 9);


-- Listar Empleados

Delimiter //
	create procedure sp_ListarEmpleados()
		Begin
			Select
				E.codigoEmpleado,
				E.numeroEmpleado,
                E.apellidosEmpleado,
                E.nombresEmpleado,
                E.direccionEmpleado,
                E.telefonoContacto,
                E.gradoCocinero,
                E.codigoTipoEmpleado
			from Empleados E;
        End //
Delimiter ;


-- Buscar Empleado

Delimiter //
	Create procedure sp_BuscarEmpleado(in id int)
		Begin
			Select
				E.codigoEmpleado,
				E.numeroEmpleado,
                E.apellidosEmpleado,
                E.nombresEmpleado,
                E.direccionEmpleado,
                E.telefonoContacto,
                E.gradoCocinero,
                E.codigoTipoEmpleado
			from Empleados E where E.codigoEmpleado = id;
        end //
Delimiter ;



-- Editar Empleado

Delimiter //
	create procedure sp_EditarEmpleado(
		in id int,
		in numero int,
        in apellidos varchar(150),
        in nombres varchar(150),
        in direccion varchar(150),
        in telefono varchar(8),
        in grado varchar(50))
        Begin
			update Empleados E set
				E.numeroEmpleado = numero,
                E.apellidosEmpleado = apellidos,
                E.nombresEmpleado = nombres,
                E.direccionEmpleado = direccion,
                E.telefonoContacto = telefono,
                E.gradoCocinero = grado
                where E.codigoEmpleado = id;
        End //
Delimiter ;



-- Eliminar Empleado

Delimiter //
	Create procedure sp_EliminarEmpleado(in id int)
		Begin
			Delete from Empleados where codigoEmpleado = id;
        End //
Delimiter ;


-- -------- SERVICIOS ---------------

select * from Servicios;
describe Servicios;

-- Agregar Servicio

Delimiter //
	Create procedure sp_AgregarServicio(
		in fecha date,
        in tipo varchar(150),
        in hora time,
        in lugar varchar(150),
        in telefono varchar(50),
        in codigo int)
		Begin
			insert into Servicios (
				fechaServicio,
                tipoServicio,
                horaServicio,
                lugarServicio,
                telefonoContacto,
                codigoEmpresa)
					values(fecha, tipo, hora, lugar, telefono, codigo);
        End //
Delimiter ;

call sp_AgregarServicio('2023-04-04', 'bufet', '12:00:00', '11 av B', '57842106', 1);
call sp_AgregarServicio('2023-05-30', 'bufet', '12:00:00', '11 av B', '45789014', 2);
call sp_AgregarServicio('2023-08-28', 'bufet', '23:00:00', '11 av B', '45983065', 4);
call sp_AgregarServicio('2023-12-22', 'bufet', '06:00:00', '11 av B', '02654716', 8);
call sp_AgregarServicio('2023-11-25', 'bufet', '15:00:00', '11 av B', '32145876', 10);
call sp_AgregarServicio('2023-10-14', 'bufet', '11:00:00', '11 av B', '36985215', 6);
call sp_AgregarServicio('2023-06-19', 'bufet', '04:00:00', '11 av B', '74125896', 7);
call sp_AgregarServicio('2023-03-18', 'bufet', '10:00:00', '11 av B', '98765412', 2);
call sp_AgregarServicio('2023-02-10', 'bufet', '21:00:00', '11 av B', '12345678', 5);
call sp_AgregarServicio('2023-01-01', 'bufet', '24:00', '11 av B', '15976304', 1);
call sp_AgregarServicio('2023-01-01', 'bufet', '24:00', 'calle 2', '15976304', 1);


-- Listar Servicios

Delimiter //
	create procedure sp_ListarServicios()
		Begin
			Select
				S.codigoServicio,
                S.fechaServicio,
                S.tipoServicio,
                S.horaServicio,
                S.lugarServicio,
                S.telefonoContacto,
                S.codigoEmpresa
			from Servicios S;
        End //
Delimiter ;


-- Buscar Servicio

Delimiter //
	Create procedure sp_BuscarServicio(in id int)
		Begin
			Select
				S.codigoServicio,
                S.fechaServicio,
                S.tipoServicio,
                S.horaServicio,
                S.lugarServicio,
                S.telefonoContacto,
                S.codigoEmpresa
			from Servicios S where S.codigoServicio = id;
        end //
Delimiter ;



-- Editar Servicio

Delimiter //
	create procedure sp_EditarServicio(
		in id int,
		in fecha date,
        in tipo varchar(150),
        in hora time,
        in lugar varchar(150),
        in telefono varchar(50))
        Begin
			update Servicios S set
                S.fechaServicio = fecha,
                S.tipoServicio = tipo,
                S.horaServicio = hora,
                S.lugarServicio = lugar,
                S.telefonoContacto = telefono
                where S.codigoServicio = id;
        End //
Delimiter ;



-- Eliminar Servicio

Delimiter //
	Create procedure sp_EliminarServicio(in id int)
		Begin
			Delete from Servicios where codigoServicio = id;
        End //
Delimiter ;


-- -------- PRESUPUESTO ---------------

select * from Presupuesto;
describe Presupuesto;

-- Agregar Presupuesto

Delimiter //
	Create procedure sp_AgregarPresupuesto(
		in fecha date,
        in cantidad decimal(10,2),
        in codigo int)
		Begin
			insert into Presupuesto (
				fechaSolicitud,
                cantidadPresupuesto,
				codigoEmpresa)
					values(fecha, cantidad, codigo);
        End //
Delimiter ;

call sp_AgregarPresupuesto('2023-04-01', 10.2, 1);
call sp_AgregarPresupuesto('2023-12-30', 10000, 1);
call sp_AgregarPresupuesto('2023-02-27', 1500, 3);
call sp_AgregarPresupuesto('2023-01-7', 560, 2);
call sp_AgregarPresupuesto('2023-04-28', 489, 4);
call sp_AgregarPresupuesto('2023-04-17', 4620.2, 10);
call sp_AgregarPresupuesto('2023-11-15', 478.2, 9);
call sp_AgregarPresupuesto('2023-10-16', 784.2, 7);
call sp_AgregarPresupuesto('2023-04-17', 369.2, 8);
call sp_AgregarPresupuesto('2023-06-26', 1230.2, 5);


-- Listar Presupuestos

Delimiter //
	create procedure sp_ListarPresupuestos()
		Begin
			Select
				P.codigoPresupuesto,
				P.fechaSolicitud,
                P.cantidadPresupuesto,
				P.codigoEmpresa
			from Presupuesto P;
        End //
Delimiter ;


-- Buscar Presupuesto

Delimiter //
	Create procedure sp_BuscarPresupuesto(in id int)
		Begin
			Select
				P.codigoPresupuesto,
				P.fechaSolicitud,
                P.cantidadPresupuesto,
				P.codigoEmpresa
			from Presupuesto P where P.codigoPresupuesto = id;
        end //
Delimiter ;



-- Editar Presupuesto

Delimiter //
	create procedure sp_EditarPresupuesto(
		in id int,
        in fecha date,
        in cantidad decimal(10,2))
        Begin
			update Presupuesto P set
				P.fechaSolicitud = fecha,
                P.cantidadPresupuesto = cantidad
                where P.codigoPresupuesto = id;
        End //
Delimiter ;



-- Eliminar Presupuesto

Delimiter //
	Create procedure sp_EliminarPresupuesto(in id int)
		Begin
			Delete from Presupuesto where codigoPresupuesto = id;
        End //
Delimiter ;


-- -------- PLATOS ---------------

select * from Platos;
describe Platos;

-- Agregar Plato

Delimiter //
	Create procedure sp_AgregarPlato(
        in cantidad int,
        in nombre varchar(150),
        in descri varchar(150),
        in pre decimal(10,2),
        in codigo int)
		Begin
			insert into Platos (
				cantidad,
                nombrePlato,
				descripcionPlato,
                precioPlato,
                codigoTipoPlato)
					values(cantidad, nombre, descri, pre, codigo);
        End //
Delimiter ;

call sp_AgregarPlato(10, 'Pollos hermanos', 'pollo frito al aire', 45.20, 1);
call sp_AgregarPlato(10, 'salsa verde', 'salsa con ingredientes verdes', 25.6, 10);
call sp_AgregarPlato(35, 'pulque', 'licor fermentado', 26, 9);
call sp_AgregarPlato(15, 'salchipapa', 'papas con salchichas', 18, 8);
call sp_AgregarPlato(16, 'jocon', 'pollo verde', 35, 1);
call sp_AgregarPlato(19, 'pepian', 'pollo rojo', 35, 2);
call sp_AgregarPlato(20, 'mixtas', 'tortilla y salchicha', 22, 4);
call sp_AgregarPlato(12, 'shuco', 'pan con lo que desee', 10, 6);
call sp_AgregarPlato(26, 'hotdog', 'pan y salchicha', 15, 3);
call sp_AgregarPlato(28, 'papas fritas', 'papas fritas al aire', 9, 7);


-- Listar Platos

Delimiter //
	create procedure sp_ListarPlatos()
		Begin
			Select
				P.codigoPlato,
				P.cantidad,
                P.nombrePlato,
				P.descripcionPlato,
                P.precioPlato,
                P.codigoTipoPlato
			from Platos P;
        End //
Delimiter ;


-- Buscar Plato

Delimiter //
	Create procedure sp_BuscarPlato(in id int)
		Begin
			Select
				P.codigoPlato,
				P.cantidad,
                P.nombrePlato,
				P.descripcionPlato,
                P.precioPlato,
                P.codigoTipoPlato
			from Platos P where P.codigoPlato = id;
        end //
Delimiter ;


-- Editar Plato

Delimiter //
	create procedure sp_EditarPlato(
		in id int,
        in canti int,
        in nombre varchar(150),
        in descri varchar(150),
        in pre decimal(10,2))
        Begin
			update Platos P set
				P.cantidad = canti,
                P.nombrePlato = nombre,
				P.descripcionPlato = descri,
                P.precioPlato = pre
                where P.codigoPlato = id;
        End //
Delimiter ;



-- Eliminar Plato

Delimiter //
	Create procedure sp_EliminarPlato(in id int)
		Begin
			Delete from Platos where codigoPlato = id;
        End //
Delimiter ;


-- -------- PRODUCTOS_HAS_PLATOS ---------------

select * from Productos_has_Platos;
describe Productos_has_Platos;

-- Agregar Producto_has_Plato

Delimiter //
	Create procedure sp_AgregarProducto_has_Plato(
        in PcProducto int,
        in cProducto int,
        in cPlato int)
		Begin
			insert into Productos_has_Platos (
				Productos_codigoProducto,
                codigoProducto,
				codigoPlato)
					values(PcProducto, cProducto, cPlato);
        End //
Delimiter ;

call sp_AgregarProducto_has_Plato(1, 2,4);
call sp_AgregarProducto_has_Plato(2, 6,6);
call sp_AgregarProducto_has_Plato(3, 7,10);
call sp_AgregarProducto_has_Plato(4, 4,1);
call sp_AgregarProducto_has_Plato(5,6,9);
call sp_AgregarProducto_has_Plato(6, 1,4);
call sp_AgregarProducto_has_Plato(7, 7,6);
call sp_AgregarProducto_has_Plato(8, 2,7);
call sp_AgregarProducto_has_Plato(9, 1,1);
call sp_AgregarProducto_has_Plato(10, 9,3);


-- Listar Productos_has_Platos

Delimiter //
	create procedure sp_ListarProductos_has_Platos()
		Begin
			Select
				P.Productos_codigoProducto,
                P.codigoProducto,
				P.codigoPlato
			from Productos_has_Platos P;
        End //
Delimiter ;


-- Buscar Producto_has_Plato

Delimiter //
	Create procedure sp_BuscarProducto_has_Plato(in id int)
		Begin
			Select
				P.Productos_codigoProducto,
                P.codigoProducto,
				P.codigoPlato
			from Productos_has_Platos P where P.Productos_codigoProducto = id;
        end //
Delimiter ;


-- Editar Producto_has_Plato

Delimiter //
	create procedure sp_EditarProducto_has_Plato(
        in PcProducto int,
        in cProducto int,
        in cPlato int)
        Begin
			update Productos_has_Platos P set
				P.Productos_codigoProducto = PcProducto,
                P.codigoProducto = cProducto,
				P.codigoPlato = cPlato
                where P.Productos_codigoProducto = PcProducto;
        End //
Delimiter ;


-- Eliminar Producto_has_Plato

Delimiter //
	Create procedure sp_EliminarProducto_has_Plato(in id int)
		Begin
			Delete from Productos_has_Platos where Productos_codigoProducto = id;
        End //
Delimiter ;

select * from productos_has_platos;


-- -------- SERVICIOS_HAS_PLATOS ---------------

select * from Servicios_has_Platos;
describe Servicios_has_Platos;

-- Agregar Servicio_has_Plato

Delimiter //
	Create procedure sp_AgregarServicio_has_Plato(
        in ScProducto int,
        in cPlato int,
        in cServicio int)
		Begin
			insert into Servicios_has_Platos (
				Servicios_codigoServicios,
                codigoPlato,
				codigoServicio)
					values(ScProducto, cPlato, cServicio);
        End //
Delimiter ;

call sp_AgregarServicio_has_Plato(1, 4,2);
call sp_AgregarServicio_has_Plato(2, 5,10);
call sp_AgregarServicio_has_Plato(3, 8,9);
call sp_AgregarServicio_has_Plato(4, 7,8);
call sp_AgregarServicio_has_Plato(5, 2,1);
call sp_AgregarServicio_has_Plato(6, 3,2);
call sp_AgregarServicio_has_Plato(7, 9,5);
call sp_AgregarServicio_has_Plato(8, 4,4);
call sp_AgregarServicio_has_Plato(9, 1,6);
call sp_AgregarServicio_has_Plato(10,10,9);


-- Listar Servicios_has_Platos

Delimiter //
	create procedure sp_ListarServicios_has_Platos()
		Begin
			Select
				S.Servicios_codigoServicios,
                S.codigoPlato,
				S.codigoServicio
			from Servicios_has_Platos S;
        End //
Delimiter ;


-- Buscar Servicio_has_Plato

Delimiter //
	Create procedure sp_BuscarServicio_has_Plato(in id int)
		Begin
			Select
				S.Servicios_codigoServicios,
                S.codigoPlato,
				S.codigoServicio
			from Servicios_has_Platos S where S.Servicios_codigoServicios = id ;
        end //
Delimiter ;


-- Editar Servicio_has_Plato

Delimiter //
	create procedure sp_EditarServicio_has_Plato(
        in ScProducto int,
        in cPlato int,
        in cServicio int)
        Begin
			update Servicios_has_Platos S set
				S.Servicios_codigoServicios = ScProducto,
                S.codigoPlato = cPlato,
				S.codigoServicio = CServicio
                where S.Servicios_codigoServicios = ScProducto;
        End //
Delimiter ;

call sp_EditarServicio_has_Plato(1,1,1);

-- Eliminar Servicio_has_Plato

Delimiter //
	Create procedure sp_EliminarServicio_has_Plato(in id int)
		Begin
			Delete from Servicios_has_Platos where Servicios_codigoServicios = id;
        End //
Delimiter ;


-- -------- SSERVICIOS_HAS_EMPLEADOS ---------------

select * from Servicios_has_Empleados;
describe Servicios_has_Empleados;

-- Agregar Servicio_has_Empleado

Delimiter //
	Create procedure sp_AgregarServicio_has_Empleado(
        in ccServicio int,
        in cServicio int,
        in cEmpleado int,
        in fecha date,
        in hora time,
        in lugar varchar(150))
		Begin
			insert into Servicios_has_Empleados (
				Servicios_codigoServicio,
                codigoServicio,
				codigoempleado,
                fechaEvento,
                horaEvento,
                lugarEvento)
					values(ccServicio, cServicio, cEmpleado, fecha, hora, lugar);
        End //
Delimiter ;

call sp_AgregarServicio_has_Empleado(1, 4,4, '2023-04-04', '10:00:00', 'casa');
call sp_AgregarServicio_has_Empleado(2, 8,5, '2023-05-30', '22:00:00', 'periferico');
call sp_AgregarServicio_has_Empleado(3, 9,6, '2023-06-14', '22:00:00', 'estacion 2');
call sp_AgregarServicio_has_Empleado(4, 1,8, '2023-12-15', '21:00:00', 'edificio');
call sp_AgregarServicio_has_Empleado(5, 6,1, '2023-11-18', '16:00:00', 'salon 09');
call sp_AgregarServicio_has_Empleado(6, 6,2, '2023-10-19', '10:00:00', 'salon 10');
call sp_AgregarServicio_has_Empleado(7, 3,10, '2023-04-20', '09:00:00', 'salon 6');
call sp_AgregarServicio_has_Empleado(8, 2,4, '2023-07-30', '19:00:00', 'salon 3');
call sp_AgregarServicio_has_Empleado(9, 10,9, '2023-08-11', '17:00:00', 'salon 2');
call sp_AgregarServicio_has_Empleado(10,5,7, '2023-06-10', '18:00:00', 'salon 1');


-- Listar Servicios_has_Empleados

Delimiter //
	create procedure sp_ListarServicios_has_Empleados()
		Begin
			Select
				S.Servicios_codigoServicio,
                S.codigoServicio,
				S.codigoempleado,
                S.fechaEvento,
                S.horaEvento,
                S.lugarEvento
			from Servicios_has_Empleados S;
        End //
Delimiter ;

call sp_ListarServicios_has_Empleados();

-- Buscar Servicio_has_Empleado

Delimiter //
	Create procedure sp_BuscarServicio_has_Empleado(in id int)
		Begin
			Select
				S.Servicios_codigoServicio,
                S.codigoServicio,
				S.codigoempleado,
                S.fechaEvento,
                S.horaEvento,
                S.lugarEvento
			from Servicios_has_Empleados S where S.Servicios_codigoServicio = id;
        end //
Delimiter ;


-- Editar Servicio_has_Empleado

Delimiter //
	create procedure sp_EditarServicio_has_Empleado(
        in ccServicio int,
        in cServicio int,
        in cEmpleado int,
        in fecha date,
        in hora time,
        in lugar varchar(150))
        Begin
			update Servicios_has_Empleados S set
				S.Servicios_codigoServicio = ccServicio,
                S.codigoServicio = cServicio,
				S.codigoempleado = cEmpleado,
                S.fechaEvento = fecha,
                S.horaEvento = hora,
                S.lugarEvento = lugar
                where S.Servicios_codigoServicio = ccServicio;
        End //
Delimiter ;


-- Eliminar Servicio_has_Empleado

Delimiter //
	Create procedure sp_EliminarServicio_has_Empleado(in id int)
		Begin
			Delete from Servicios_has_Empleados where Servicios_codigoServicio = id;
        End //
Delimiter ;


-- ------------------------------------------

Delimiter //
	create procedure sp_ReporteServicios(in id int)
		begin
			Select
				E.nombreEmpresa,
                P.cantidadPresupuesto,
                S.fechaServicio,
                S.tipoServicio,
                EM.nombresEmpleado,
                EM.apellidosEmpleado,
                TE.descripcion,
                PL.nombrePlato,
                PR.nombreProducto
                from Empresas E inner join Presupuesto P on E.codigoEmpresa = P.codigoEmpresa
					inner join Servicios S on E.codigoEmpresa = S.codigoEmpresa
						inner join Servicios_has_Empleados SE on S.codigoServicio = SE.codigoServicio
							inner join Empleados EM on SE.codigoEmpleado = EM.codigoEmpleado
								inner join TipoEmpleado TE on EM.codigoTipoEmpleado = TE.codigoTipoEmpleado
					inner join Servicios_has_platos SP on S.codigoServicio = SP.codigoServicio
						inner join Platos PL on SP.codigoPlato = PL.codigoPlato
							inner join Productos_has_Platos PP on PL.codigoPlato = PP.codigoPlato
								inner join Productos PR on PP.codigoProducto = PR.codigoProducto
                where E.codigoEmpresa = id
				group by(S.fechaServicio);
        end //
Delimiter ;

call sp_reporteServicios(1);

-- ----------- USUARIO -----
Delimiter //
	Create procedure sp_AgregarUsuario(
		in nombre varchar(100),
        in apellido varchar(100),
        in usuario varchar(50),
        in contrasena varchar(50))
	Begin
		Insert into Usuario(nombreUsuario,apellidoUsuario,usuarioLogin,contrasena)
			values(nombre,apellido,usuario, contrasena);
    End //
Delimiter ;

Delimiter //
	Create procedure sp_ListarUsuarios()
		Begin
			Select
				U.codigoUsuario,
				U.nombreUsuario,
				U.apellidoUsuario,
				U.usuarioLogin,
				U.contrasena
            from Usuario U;
        End //
Delimiter ;

call sp_AgregarUsuario('Rodrigo','Díaz','admin','e00cf25ad42683b3df678c61f42c6bda'); 
call sp_AgregarUsuario('Pedro','Armas','parmas','68336db610aeb0cc5240e13b701145c2'); 


call sp_ListarUsuarios();

