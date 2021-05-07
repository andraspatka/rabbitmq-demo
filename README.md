# rabbitmq-demo

Demo project using 3 microservices which are connected with rabbitmq. All services are dockerized and can be deployed to K8s using skaffold/helm

Assignment:
1. Develop 3 web services A, B, C, each in a different technology (python, php, .net core or java).
2. Service A pushes data into RabbitMQ whenever it receives a request, which broadcasts data to B and C in parallel. B and C only display a log when data was fetched from the queue
3. Run 3 instances of Service A
4. Add a loadbalancer based on nginx in front of the 3 instances
5. All services should be containerized via Docker

# Setting up

## Requirements

- Skaffold: https://skaffold.dev/docs/install/
- Helm: https://helm.sh/docs/intro/install/
- Kubernetes cluster (minikube, k3s, Docker-desktop, kind, etc.)
- Docker
- kubectl (comes with Docker-desktop, can also be installed separately)

## Kubeapps

Kubeapps is recommended as it provides a dashboard for K8s. It is not required, everything that can be done with Kubeapps can be done with kubectl.

```bash
# Installing kubeapps in namespace "kubeapps" (recommended to install it in a different namespace, as it starts a lot of pods)
helm repo add bitnami https://charts.bitnami.com/bitnami
kubectl create namespace kubeapps 
helm install kubeapps --namespace kubeapps bitnami/kubeapps

# Wait for all pods to be running, check the status with:
kubectl get pods --namespace kubeapps

# Once everything is running, run a port forward command in order to be able to access kubeapps:
kubectl port-forward --namespace kubeapps service/kubeapps 8080:80

# For the login, do the following steps (this is only recommended for local development, for production, setup an oidc mechanism, see: https://github.com/kubeapps/kubeapps/)
kubectl create serviceaccount kubeapps-operator
# giving a service-account a cluster-admin role effectively disables RBAC, as cluster-admin has all permissions.
kubectl create clusterrolebinding kubeapps-operator --clusterrole=cluster-admin --serviceaccount=default:kubeapps-operator

# Get the token for the service account
kubectl get -n default secret $(kubectl get -n default serviceaccount kubeapps-operator -o jsonpath='{.secrets[].name}') -o go-template='{{.data.token | base64decode}}' && echo

# Uninstalling:
helm uninstall kubeapps --namespace kubeapps
# Deleting the service account
kubectl delete serviceaccount kubeapps-operator
# Deleting the namespace
kubectl delete namespace kubeapps
```

## Kubernetes Web Ui (Dashboard)

Kubeapps as a dashboard is somewhat limited, e.g. you can't view the logs of a pod. An alternative to it is the official Kubernetes Web Ui Dashboard: https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/

```bash
# Deploying the dashboard
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0/aio/deploy/recommended.yaml

# create service account
kubectl apply -f dashboard/dashboard-adminuser.yaml

# create role bindings for the service account
kubectl apply -f dashboard/adminuser-clusterrolebinding.yaml

# get the jwt required for logging in
kubectl -n kubernetes-dashboard get secret $(kubectl -n kubernetes-dashboard get sa/admin-user -o jsonpath="{.secrets[0].name}") -o go-template="{{.data.token | base64decode}}"

# make the dashboard accessible
kubectl proxy
```

The dashboard will be accessible at the following url: http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/

# Deployments

All deployments are managed with skaffold. Skaffold is an open source project from Google which handles deployments to a K8s cluster. The usage of it in this project is a sort of wrapper around helm.

## Rabbitmq

Deploying rabbitmq is done with the help of the bitnami Rabbitmq chart: https://github.com/bitnami/charts/tree/master/bitnami/rabbitmq

```bash
# Add the bitnami charts helm repository to your local helm repositories
helm repo add bitnami https://charts.bitnami.com/bitnami

# Deploy with the help of Skaffold (in root)
skaffold run -m rabbitmq
# or if you're in the rabbitmq folder, simply:
skaffold run

# port forward in order to access rabbitmq from outside the cluster:
kubectl port-forward --namespace default svc/rabbitmq 5672:5672

# port forward for the rabbitmq management interface:
kubectl port-forward --namespace default svc/rabbitmq 15672:15672

# get password for rabbitmq
echo "Password      : $(kubectl get secret --namespace default rabbitmq -o jsonpath="{.data.rabbitmq-password}" | base64 --decode)"
```