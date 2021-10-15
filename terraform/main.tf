terraform {
  required_providers {
    
    aws = {
      source = "hashicorp/aws"
      version = "~> 3.54.0"
    }

    random = {
      source = "hashicorp/random"
      version = "3.1.0"
    }
  }
}

provider "aws" {
    region = var.region
}

provider "random" {

}

// TODO: Substituir account number nos scripts
// TODO: Remover key pair id do cluster