variable project_name {
    type = string
    description = "This project's name, Sprello!"
    default = "sprello"
}

variable "region" {
    type = string
    description = "AWS region to deploy"
    default = "sa-east-1"
}

variable "availability_zones" {
    type = list(string)
    description = "AWS availability zones"
    default = ["sa-east-1a", "sa-east-1c"]
}

variable "vpc_cidr_block" {
    type = string
    description = "CIDR block for AWS VPC"
    default = "10.0.0.0/16"
}

variable "public_subnet_cidr_block" {
    type = string
    description = "CIDR block for AWS VPC's public subnet"
    default = "10.0.0.0/24"
}

variable "private_subnet_cidr_block" {
    type = list(string)
    description = "CIDR block for AWS VPC's private subnet"
    default = ["10.0.1.0/24", "10.0.2.0/24"]
}