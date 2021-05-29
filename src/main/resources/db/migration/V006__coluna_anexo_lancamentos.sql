-- Column: public.lancamento.anexo

-- ALTER TABLE public.lancamento DROP COLUMN anexo;

ALTER TABLE public.lancamento
    ADD COLUMN anexo character varying(200) COLLATE pg_catalog."default";