#################################
##########  PASSWORD  ###########
#################################

/*resource "random_password" "random_db_password" {
    length = 24
    special = true
    override_special = "@!#$"
}

resource "aws_secretsmanager_secret" "db_password_secret" {
    name = "db_password_secret"
}

resource "aws_secretsmanager_secret_version" "db_password_secret_value" {
    secret_id = aws_secretsmanager_secret.db_password_secret.id
    secret_string = random_password.random_db_password.result
}*/

#################################
######### DB NETWORK  ###########
#################################

resource "aws_db_subnet_group" "db_subnet_group" {
    name = "${var.project_name}-subnet-group"
    description = "${var.project_name}'s subnet group"
    subnet_ids = aws_subnet.private_subnet[*].id
}

resource "aws_security_group" "db_security_group" {
  name = "${var.project_name}-db-security-group"
  description = "Allow DB connection inbound traffic from public subnet"
  vpc_id = aws_vpc.vpc.id

  ingress {
    description = "Database port ingress from public subnet"
    from_port = 5432
    to_port = 5432
    protocol = "tcp"
    cidr_blocks = [aws_subnet.public_subnet.cidr_block]
  }

  ingress {
      from_port = 0
      to_port = 0
      protocol = "-1"
      self = true
  }

  tags = {
    "Name" = "${var.project_name}-db-security-group"
  }
}

#################################
##########  DATABASE  ###########
#################################

resource "aws_db_instance" "db" {
    
    name = "${var.project_name}db"

    engine = "postgres"
    engine_version = "13.3"
    instance_class = "db.t3.micro"
    
    identifier = "${var.project_name}-db"
    username = "${var.project_name}"
    //password = random_password.random_db_password.result
    password = "sprello123"
    port = 5432
    
    storage_type = "gp2"
    allocated_storage = 20
    max_allocated_storage = 21

    db_subnet_group_name = aws_db_subnet_group.db_subnet_group.name
    vpc_security_group_ids = [aws_security_group.db_security_group.id]

    skip_final_snapshot = true
}

output "db_jdbc_connection_string" {
    value = "jdbc:postgresql://${aws_db_instance.db.endpoint}/${aws_db_instance.db.name}"
}