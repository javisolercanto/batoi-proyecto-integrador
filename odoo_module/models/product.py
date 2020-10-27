# -*- coding: utf-8 -*-
from openerp import models, fields, api

class product(models.Model):
    _name = 'batoi_logic.product'

    name = fields.Char(String='Product Name', size=30, required=True)
    price = fields.Float(String='Price', digits=(6, 2), required=True)
    kg = fields.Float(String='Kilograms', digits=(6, 2))
    description = fields.Text(String='Description')
    image = fields.Binary(String='Image')
    order_lines_id = fields.One2many('batoi_logic.order_line', 'product_id', 'Associated Order lines')
    supply_provider_id = fields.One2many('batoi_logic.supplier_order', 'product_id', 'Actual Suppliers')
    providers_available = fields.Many2many('batoi_logic.provider', string="Providers")

    _sql_constraints = [
        ('batoi_logic_unique_name', 'UNIQUE (name)', 'The name of the product must be unique')]
