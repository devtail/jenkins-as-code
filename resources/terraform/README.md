# Terraform for Slave Infrastructure

## Place variables/secrets in config/

Secrets are not commited to Github ;-)

Create `config/` dir with secrets/variables for terraform automation. 
Directory structure should in the end look like that:

```
terraform/
├── aws
│   ├── slave-network
│   │   ├── all.tf
│   │   ├── aws-slave-network.backend.config -> ../../config/aws-slave-network.backend.config                                            
│   │   ├── io.tf
│   │   └── terraform.tfvars -> ../../config/aws-slave-network.tfvars                                                                    
│   └── slave-vms
│       ├── all.tf
│       ├── aws-slave-vms.backend.config -> ../../config/aws-slave-vms.backend.config                                                    
│       ├── io.tf
│       └── terraform.tfvars -> ../../config/aws-slave-vms.tfvars
├── config
│   ├── aws-slave-network.backend.config
│   ├── aws-slave-network.tfvars
│   ├── aws-slave-vms.backend.config
│   └── aws-slave-vms.tfvars
└── README.md
```

## Slave Network Infrastructure

To initialize the state:
```
cd aws/slave-network
terraform init -backend-config aws-slave-network.backend.config
```

To bootstrap the network:
```
cd aws/slave-network
terraform apply
```

## Slave VMs

Jenkins can use the `slave-vms` module to bootstrap/shutdown slave VMs on-demand. 
A call in jenkins could look like that:
```
cd aws/slave-vms
terraform apply
```
