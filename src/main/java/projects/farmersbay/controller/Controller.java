package projects.farmersbay.controller;

public abstract class Controller<A> {
    public abstract void create(A object);
    public abstract void update(A object);
    public abstract void delete(Integer id);
    public abstract A read(Integer id);
}