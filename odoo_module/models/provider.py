# -*- coding: utf-8 -*-
from openerp import models, fields, api

class provider(models.Model):
    _name = 'batoi_logic.provider'

    name = fields.Char(string='Provider Name', size=40, required=True)
    cif = fields.Char(String='CIF', size=9, required=True)
    email = fields.Char(string='E-mail', size=30)
    telephone = fields.Integer(String='Phone', size=9)
    supplier_orders_id = fields.One2many('batoi_logic.supplier_order', 'provider_id', 'Supplier Orders')
    products_supplied = fields.Many2many('batoi_logic.supplier_order', string="Products that are supplied")

    _sql_constraints = [
        ('batoi_logic_unique_name_unique_cif', 'UNIQUE (cif)', 'CIF must be unique'),
        ('batoi_logic_unique_name_unique_cif', 'UNIQUE (name)', 'Company name must be unique'),
    ]
