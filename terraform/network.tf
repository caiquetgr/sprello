resource "aws_vpc" "vpc" {
    cidr_block = var.vpc_cidr_block
    enable_dns_hostnames = true
    enable_dns_support = true

    tags = {
      Name = "${var.project_name}-vpc"
    }
}

resource "aws_internet_gateway" "ig" {
  vpc_id = aws_vpc.vpc.id

  tags = {
    Name = "${var.project_name}-internet-gateway"
  }
}

resource "aws_subnet" "public_subnet" {
  vpc_id = aws_vpc.vpc.id
  availability_zone = var.availability_zones[0]
  cidr_block = var.public_subnet_cidr_block
  map_public_ip_on_launch = true
  
  tags = {
    Name = "${var.project_name}-public-subnet"
  }
}

resource "aws_route_table" "public_route_table" {
  vpc_id = aws_vpc.vpc.id

  tags = {
    Name = "${var.project_name}-public-route-table"
  }
}

resource "aws_route" "route_to_internet_gateway" {
  route_table_id = aws_route_table.public_route_table.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id = aws_internet_gateway.ig.id
}

resource "aws_route_table_association" "public_route_table_association" {
  subnet_id      = aws_subnet.public_subnet.id
  route_table_id = aws_route_table.public_route_table.id
}

resource "aws_subnet" "private_subnet" {
  count = length(var.availability_zones)
  vpc_id = aws_vpc.vpc.id
  availability_zone = var.availability_zones[count.index]
  cidr_block = var.private_subnet_cidr_block[count.index]
  
  tags = {
    Name = "${var.project_name}-private-subnet-${count.index}"
  }
}

resource "aws_route_table" "private_route_table" {
  vpc_id = aws_vpc.vpc.id
  
  tags = {
    Name = "${var.project_name}-private-route-table"
  }
}

resource "aws_route_table_association" "private_route_table_association" {
  count = length(aws_subnet.private_subnet)
  subnet_id = aws_subnet.private_subnet[count.index].id
  route_table_id = aws_route_table.private_route_table.id
}

resource "aws_security_group" "ecs_security_group" {
  name = "${var.project_name}-ecs-security-group"
  description = "ECS security group"
  vpc_id = aws_vpc.vpc.id

  ingress {
    description = "Ingress rule"
    from_port = 80
    to_port = 80
    protocol = "tcp"
    cidr_blocks = ["${var.my-ip}"]
  }

  ingress {
    description = "Ingress rule"
    from_port = 22
    to_port = 22
    protocol = "tcp"
    cidr_blocks = ["${var.my-ip}"]
  }

  egress {
    description = "Egress rule"
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    "Name" = "${var.project_name}-ecs-security-group"
  }
}


resource "aws_security_group" "alb_security_group" {
  name = "${var.project_name}-alb-security-group"
  description = "ALB security group"
  vpc_id = aws_vpc.vpc.id

  ingress {
    description = "Ingress rule"
    from_port = 80
    to_port = 80
    protocol = "tcp"
    cidr_blocks = ["${var.my-ip}"]
  }

  egress {
    description = "Egress rule"
    from_port = 8080
    to_port = 8080
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    "Name" = "${var.project_name}-alb-security-group"
  }
}