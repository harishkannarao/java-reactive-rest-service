CREATE TABLE customer_table (
    data jsonb
);

CREATE UNIQUE INDEX unique_index_jsonb_id ON customer_table using btree ((cast(data->>'id' as UUID)));